import { useEffect, useMemo, useRef } from 'react';
import mapboxgl from 'mapbox-gl';
import { Mountain, Moon, Sun } from 'lucide-react';
import 'mapbox-gl/dist/mapbox-gl.css';

const MAPBOX_TOKEN = import.meta.env.VITE_MAPBOX_ACCESS_TOKEN;

const toCoordinates = (route) => {
    if (!route) return [];
    if (route.leafletCoordinates?.length) {
        return route.leafletCoordinates.map(([lat, lng]) => [lng, lat]);
    }
    return route.geometry?.coordinates || [];
};

export default function MapboxTerrainMap({ primaryRoute, alternativeRoute, vehicles = [], hazards = [], height = '600px', onModeChange }) {
    const containerRef = useRef(null);
    const mapRef = useRef(null);
    const primaryGeometryKey = JSON.stringify(primaryRoute?.leafletCoordinates || primaryRoute?.geometry?.coordinates || []);
    const alternativeGeometryKey = JSON.stringify(alternativeRoute?.leafletCoordinates || alternativeRoute?.geometry?.coordinates || []);
    const primaryCoords = useMemo(() => toCoordinates(primaryRoute), [primaryGeometryKey]);
    const alternativeCoords = useMemo(() => toCoordinates(alternativeRoute), [alternativeGeometryKey]);
    const primaryRouteColor = primaryRoute?.riskScore > 0.6 ? '#ef4444' : '#3b82f6';
    const hasInitializedCamera = useRef(false);

    useEffect(() => {
        if (!containerRef.current || !MAPBOX_TOKEN) return undefined;

        mapboxgl.accessToken = MAPBOX_TOKEN;
        const map = new mapboxgl.Map({
            container: containerRef.current,
            style: 'mapbox://styles/mapbox/satellite-streets-v12',
            center: [92.5, 26.14],
            zoom: 6.5,
            pitch: 62,
            bearing: -12,
            antialias: true,
            projection: 'globe'
        });
        mapRef.current = map;
        map.addControl(new mapboxgl.NavigationControl(), 'top-right');
        map.addControl(new mapboxgl.FullscreenControl(), 'top-right');

        map.on('style.load', () => {
            map.setFog({
                color: 'rgb(12, 25, 43)',
                'high-color': 'rgb(20, 52, 83)',
                'horizon-blend': 0.18,
                'space-color': 'rgb(5, 12, 24)',
                'star-intensity': 0.12
            });
        });

        map.on('load', () => {
            map.addSource('mapbox-dem', {
                type: 'raster-dem',
                url: 'mapbox://mapbox.mapbox-terrain-dem-v1',
                tileSize: 512,
                maxzoom: 14
            });
            map.setTerrain({ source: 'mapbox-dem', exaggeration: 1.35 });

            const firstSymbolLayer = map.getStyle().layers.find(layer => layer.type === 'symbol')?.id;
            if (map.getSource('composite') && !map.getLayer('3d-buildings')) {
                map.addLayer({
                    id: '3d-buildings',
                    type: 'fill-extrusion',
                    source: 'composite',
                    'source-layer': 'building',
                    minzoom: 12,
                    filter: ['!=', ['get', 'extrude'], 'false'],
                    paint: {
                        'fill-extrusion-color': [
                            'interpolate', ['linear'], ['get', 'height'],
                            0, '#243b53',
                            25, '#3b6385',
                            80, '#6b8eaa'
                        ],
                        'fill-extrusion-height': [
                            'interpolate', ['linear'], ['zoom'],
                            12, 0,
                            15.5, ['coalesce', ['get', 'height'], 0]
                        ],
                        'fill-extrusion-base': ['coalesce', ['get', 'min_height'], 0],
                        'fill-extrusion-opacity': 0.82
                    }
                }, firstSymbolLayer);
            }

            const addRoute = (id, coordinates, color, width) => {
                if (!coordinates.length) return;
                map.addSource(id, { type: 'geojson', data: { type: 'Feature', geometry: { type: 'LineString', coordinates } } });
                map.addLayer({ id, type: 'line', source: id, paint: { 'line-color': color, 'line-width': width, 'line-opacity': 0.9 } });
            };

            addRoute('primary-route', primaryCoords, primaryRouteColor, 6);
            addRoute('alternative-route', alternativeCoords, '#10b981', 5);

            const points = [
                ...(primaryCoords.length ? [{ coordinates: primaryCoords[0], color: '#3b82f6', label: 'Origin' }, { coordinates: primaryCoords.at(-1), color: '#ef4444', label: 'Destination' }] : []),
                ...vehicles.filter(vehicle => vehicle.currentLatitude && vehicle.currentLongitude).map(vehicle => ({ coordinates: [vehicle.currentLongitude, vehicle.currentLatitude], color: '#10b981', label: vehicle.vehicleNumber || 'Vehicle' })),
                ...hazards.filter(hazard => hazard.latitude && hazard.longitude).map(hazard => ({ coordinates: [hazard.longitude, hazard.latitude], color: '#ef4444', label: hazard.type || 'Hazard' }))
            ];

            points.forEach(point => {
                const marker = document.createElement('button');
                marker.type = 'button';
                marker.setAttribute('aria-label', point.label);
                marker.title = point.label;
                marker.style.cssText = `width:18px;height:18px;border-radius:50%;border:3px solid white;background:${point.color};box-shadow:0 0 14px ${point.color};cursor:pointer;`;
                new mapboxgl.Marker({ element: marker }).setLngLat(point.coordinates).addTo(map);
            });

            if (primaryCoords.length && !hasInitializedCamera.current) {
                const bounds = primaryCoords.reduce((result, coordinate) => result.extend(coordinate), new mapboxgl.LngLatBounds(primaryCoords[0], primaryCoords[0]));
                map.fitBounds(bounds, { padding: 70, pitch: 62, bearing: -12, maxZoom: 9, duration: 800 });
                hasInitializedCamera.current = true;
            }
        });

        return () => {
            map.remove();
            mapRef.current = null;
        };
    }, [primaryGeometryKey, alternativeGeometryKey, primaryRouteColor, primaryCoords, alternativeCoords]);

    if (!MAPBOX_TOKEN) {
        return (
            <div className="relative h-full w-full" style={{ height }}>
                <div className="flex h-full min-h-48 items-center justify-center bg-slate-900 p-6 text-center text-sm text-slate-300" role="status">
                    Add <code className="mx-1 rounded bg-slate-800 px-1.5 py-0.5 text-cyan-300">VITE_MAPBOX_ACCESS_TOKEN</code> to <code className="mx-1 rounded bg-slate-800 px-1.5 py-0.5 text-cyan-300">frontend/.env</code> to enable the Mapbox 3D view.
                </div>
                <ModeMenu onModeChange={onModeChange} />
            </div>
        );
    }

    return (
        <div className="relative h-full w-full" style={{ height }}>
            <div ref={containerRef} className="h-full w-full" role="img" aria-label="Mapbox 3D terrain map with logistics routes and hazards" />
            <ModeMenu onModeChange={onModeChange} />
        </div>
    );
}

function ModeMenu({ onModeChange }) {
    return (
        <div className="absolute top-4 right-4 z-10 flex items-center gap-1 rounded-xl bg-slate-900/95 p-1 shadow-xl" role="group" aria-label="Map display mode">
            <button type="button" onClick={() => onModeChange?.('normal')} className="p-2 rounded-lg text-slate-300 hover:bg-slate-700 hover:text-white" title="Normal map" aria-label="Normal map"><Sun className="w-4 h-4" /></button>
            <button type="button" onClick={() => onModeChange?.('terrain')} className="p-2 rounded-lg text-slate-300 hover:bg-emerald-600 hover:text-white" title="Terrain map" aria-label="Terrain map"><Mountain className="w-4 h-4" /></button>
            <button type="button" onClick={() => onModeChange?.('3d')} className="px-2 py-2 rounded-lg bg-cyan-600 text-white text-[10px] font-bold" title="3D terrain view" aria-label="3D terrain view" aria-pressed="true">3D</button>
            <button type="button" onClick={() => onModeChange?.('dark')} className="p-2 rounded-lg text-slate-300 hover:bg-slate-700 hover:text-white" title="Dark map" aria-label="Dark map"><Moon className="w-4 h-4" /></button>
        </div>
    );
}
