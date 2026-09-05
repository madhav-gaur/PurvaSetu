import { useEffect, useState } from 'react';
import {
  MapContainer,
  TileLayer,
  Polyline,
  Marker,
  Popup,
  Tooltip,
  useMap
} from 'react-leaflet';
import L from 'leaflet';
import { Mountain, Moon, Sun } from 'lucide-react';
import MapboxTerrainMap from './MapboxTerrainMap';

// Fix for custom SVG markers to avoid default Leaflet PNG missing image issues
const createDivIcon = (svgString, className = '', options = {}) => {
  return L.divIcon({
    html: svgString,
    className: `custom-marker-icon ${className}`,
    iconSize: options.iconSize || [36, 36],
    iconAnchor: options.iconAnchor || [18, 18],
    popupAnchor: [0, -18],
  });
};

const vehicleIcon = (status = 'IN_TRANSIT') => createDivIcon(`
  <div style="background: ${status === 'IN_TRANSIT' ? '#10B981' : '#64748B'}; width: 34px; height: 34px; border-radius: 50%; border: 3px solid #FFFFFF; display: flex; align-items: center; justify-content: center; box-shadow: 0 0 12px rgba(16, 185, 129, 0.6);">
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="#FFFFFF" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
      <path d="M14 18V6a2 2 0 0 0-2-2H4a2 2 0 0 0-2 2v11a1 1 0 0 0 1 1h2"/>
      <path d="M15 18H9"/>
      <path d="M19 18h2a1 1 0 0 0 1-1v-5l-3-4h-5v10Z"/>
      <circle cx="7" cy="18" r="2"/>
      <circle cx="17" cy="18" r="2"/>
    </svg>
  </div>
`);

const landslideIcon = createDivIcon(`
  <div style="background: #EF4444; width: 32px; height: 32px; border-radius: 8px; border: 2px solid #FEE2E2; display: flex; align-items: center; justify-content: center; box-shadow: 0 0 14px rgba(239, 68, 68, 0.7); animation: pulse 2s infinite;">
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="#FFFFFF" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
      <path d="m21.73 18-8-14a2 2 0 0 0-3.48 0l-8 14A2 2 0 0 0 4 21h16a2 2 0 0 0 1.73-3Z"/>
      <line x1="12" y1="9" x2="12" y2="13"/>
      <line x1="12" y1="17" x2="12.01" y2="17"/>
    </svg>
  </div>
`);

const floodIcon = createDivIcon(`
  <div style="background: #0284C7; width: 32px; height: 32px; border-radius: 8px; border: 2px solid #E0F2FE; display: flex; align-items: center; justify-content: center; box-shadow: 0 0 14px rgba(2, 132, 199, 0.7);">
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="#FFFFFF" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
      <path d="M4 14.899A7 7 0 1 1 15.71 8h1.79a4.5 4.5 0 0 1 2.5 8.242"/>
      <path d="M16 14v6"/>
      <path d="M8 14v6"/>
      <path d="M12 16v6"/>
    </svg>
  </div>
`);

const blockIcon = createDivIcon(`
  <div style="background: #DC2626; width: 32px; height: 32px; border-radius: 8px; border: 2px solid #FFFFFF; display: flex; align-items: center; justify-content: center; box-shadow: 0 0 14px rgba(220, 38, 38, 0.8);">
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="#FFFFFF" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
      <circle cx="12" cy="12" r="10"/>
      <line x1="4.93" y1="4.93" x2="19.07" y2="19.07"/>
    </svg>
  </div>
`);

const pinIcon = (label, color = '#10B981') => createDivIcon(`
  <div style="width: 250px; box-sizing: border-box; background: rgba(8, 18, 32, 0.96); color: #F8FAFC; font-weight: 700; font-size: 11px; line-height: 16px; padding: 5px 9px; border-radius: 8px; border: 1px solid ${color}; display: flex; align-items: center; gap: 6px; box-shadow: 0 4px 14px rgba(0,0,0,0.55); white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
    <span style="width: 8px; height: 8px; flex: 0 0 auto; border-radius: 50%; background: ${color}; border: 1px solid #FFFFFF;"></span>
    <span style="overflow: hidden; text-overflow: ellipsis;">${label}</span>
  </div>
`, 'endpoint-label', { iconSize: [250, 40], iconAnchor: [12, 40] });

// Auto-adjust view when bounds change
function MapViewUpdater({ primaryRouteCoords, altRouteCoords }) {
  const map = useMap();
  useEffect(() => {
    const coords = [];
    if (primaryRouteCoords && primaryRouteCoords.length > 0) coords.push(...primaryRouteCoords);
    if (altRouteCoords && altRouteCoords.length > 0) coords.push(...altRouteCoords);

    if (coords.length > 0) {
      try {
        const bounds = L.latLngBounds(coords);
        map.fitBounds(bounds, { padding: [50, 50], maxZoom: 10 });
      } catch (e) {
        throw new console.error(e);

      }
    }
  }, [primaryRouteCoords, altRouteCoords, map]);
  return null;
}

export default function LeafletMap({
  primaryRoute,
  alternativeRoute,
  vehicles = [],
  reports = [],
  hazards = [],
  // advisories = [],
  // selectedVehicle = null,
  onSelectVehicle,
  originName = 'Guwahati Logistics Hub',
  destinationName = 'Silchar Medical Hub',
  height = "600px"
}) {
  const [mapMode, setMapMode] = useState('normal');

  // Convert coordinates to Leaflet [lat, lng] format
  const extractLeafletCoords = (route) => {
    if (!route) return [];
    if (route.leafletCoordinates && route.leafletCoordinates.length > 0) {
      return route.leafletCoordinates;
    }
    if (route.geometry && route.geometry.coordinates) {
      // GeoJSON is [lon, lat] -> convert to [lat, lon]
      return route.geometry.coordinates.map(pt => [pt[1], pt[0]]);
    }
    return [];
  };

  const primaryCoords = extractLeafletCoords(primaryRoute);
  const altCoords = extractLeafletCoords(alternativeRoute);

  if (mapMode === '3d') {
    return (
      <div
        className="relative w-full overflow-hidden rounded-2xl border border-cyan-900/70 bg-[#0B0F19] shadow-2xl"
        style={{ height }}
        role="region"
        aria-label="Interactive Mapbox 3D terrain map"
      >
        <MapboxTerrainMap
          primaryRoute={primaryRoute}
          alternativeRoute={alternativeRoute}
          vehicles={vehicles}
          hazards={hazards}
          height={height}
          onModeChange={setMapMode}
        />
      </div>
    );
  }

  // Default Center of NER: Lat 26.2, Lng 92.5
  const nerCenter = [26.1445, 92.5000];

  return (
    <div
      className={`relative w-full rounded-2xl overflow-hidden border border-slate-800 shadow-2xl bg-[#0B0F19] ${mapMode === 'dark' ? 'map-theme-dark' : ''} ${mapMode === 'terrain' ? 'map-theme-terrain' : ''} ${mapMode === '3d' ? 'map-theme-3d' : ''}`}
      style={{ height }}
      role="region"
      aria-label="Interactive North Eastern Region disaster and logistics map"
    >
      <MapContainer
        center={nerCenter}
        zoom={7}
        scrollWheelZoom={true}
        className={`w-full h-full ${mapMode === '3d' ? 'leaflet-map-3d' : ''}`}
      >
        {/* Public light and dark tile layers */}
        <TileLayer
          key={`${mapMode}-map-tiles`}
          attribution={mapMode === 'dark'
            ? '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors &copy; <a href="https://carto.com/attributions">CARTO</a>'
            : mapMode === 'terrain' || mapMode === '3d'
              ? '&copy; <a href="https://opentopomap.org">OpenTopoMap</a> contributors'
              : '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'}
          url={mapMode === 'dark'
            ? 'https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png'
            : mapMode === 'terrain' || mapMode === '3d'
              ? 'https://{s}.tile.opentopomap.org/{z}/{x}/{y}.png'
              : 'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png'}
          subdomains="abc"
          maxZoom={19}
        />

        <MapViewUpdater primaryRouteCoords={primaryCoords} altRouteCoords={altCoords} />

        {/* Primary Route Polyline */}
        {primaryCoords.length > 0 && (
          <Polyline
            positions={primaryCoords}
            pathOptions={{
              color: primaryRoute?.riskScore > 0.6 ? '#EF4444' : '#3B82F6',
              weight: primaryRoute?.isRecommended ? 6 : 4,
              opacity: 0.85,
              dashArray: primaryRoute?.status === 'HIGH_RISK_AVOID' ? '8, 8' : null,
            }}
          >
            <Tooltip sticky>
              <div className="text-xs p-1">
                <p className="font-bold text-slate-100">{primaryRoute.routeName || 'Primary Route'}</p>
                <p className="text-slate-300">Distance: {Math.round(primaryRoute.distance)} km | Time: {primaryRoute.formattedDuration}</p>
                <p className={`font-semibold ${primaryRoute.riskScore > 0.6 ? 'text-red-400' : 'text-emerald-400'}`}>
                  Risk Score: {Math.round((primaryRoute.riskScore || 0) * 100)}% ({primaryRoute.riskLevel || 'MEDIUM'})
                </p>
              </div>
            </Tooltip>
          </Polyline>
        )}

        {/* Alternative / Bypass Route Polyline */}
        {altCoords.length > 0 && (
          <Polyline
            positions={altCoords}
            pathOptions={{
              color: '#10B981', // Emerald for safe bypass
              weight: alternativeRoute?.isRecommended ? 6 : 4,
              opacity: 0.9,
            }}
          >
            <Tooltip sticky>
              <div className="text-xs p-1">
                <p className="font-bold text-emerald-400">{alternativeRoute.routeName || 'Alternative Bypass Corridor'}</p>
                <p className="text-slate-300">Distance: {Math.round(alternativeRoute.distance)} km | Time: {alternativeRoute.formattedDuration}</p>
                <p className="font-semibold text-emerald-300">
                  Risk Score: {Math.round((alternativeRoute.riskScore || 0) * 100)}% ({alternativeRoute.riskLevel || 'LOW'})
                </p>
                <p className="text-[10px] text-slate-300 mt-1">Recommended Safe Corridor</p>
              </div>
            </Tooltip>
          </Polyline>
        )}

        {/* Origin Marker */}
        {primaryCoords.length > 0 && (
          <Marker position={primaryCoords[0]} icon={pinIcon(`Origin: ${originName}`, '#3B82F6')}>
            <Popup>
              <div className="text-xs">
                <p className="font-bold text-slate-200">{originName}</p>
                <p className="text-slate-400">Assam Freight Consolidation Center</p>
              </div>
            </Popup>
          </Marker>
        )}

        {/* Destination Marker */}
        {primaryCoords.length > 0 && (
          <Marker position={primaryCoords[primaryCoords.length - 1]} icon={pinIcon(`Destination: ${destinationName}`, '#EF4444')}>
            <Popup>
              <div className="text-xs">
                <p className="font-bold text-slate-200">{destinationName}</p>
                <p className="text-slate-400">Cachar District Consignment Depot</p>
              </div>
            </Popup>
          </Marker>
        )}

        {/* Vehicles Markers */}
        {vehicles.map((v) => {
          if (!v.currentLatitude || !v.currentLongitude) return null;
          return (
            <Marker
              key={v.id || v.vehicleNumber}
              position={[v.currentLatitude, v.currentLongitude]}
              icon={vehicleIcon(v.status)}
              eventHandlers={{
                click: () => onSelectVehicle && onSelectVehicle(v)
              }}
            >
              <Popup>
                <div className="text-xs p-1 space-y-1">
                  <div className="flex items-center justify-between gap-2 border-b border-slate-700 pb-1">
                    <span className="font-bold text-emerald-400">{v.vehicleNumber}</span>
                    <span className="px-1.5 py-0.5 rounded bg-slate-800 text-[10px] font-mono text-slate-300">
                      {v.status}
                    </span>
                  </div>
                  <p className="text-slate-300"><strong className="text-slate-400">Model:</strong> {v.vehicleType}</p>
                  <p className="text-slate-300"><strong className="text-slate-400">Driver:</strong> {v.driver} ({v.driverPhone})</p>
                  <p className="text-slate-300"><strong className="text-slate-400">Speed:</strong> {v.speed} km/h</p>
                  {v.currentShipment && (
                    <p className="text-amber-300 font-semibold mt-1">
                      Consignment: {v.currentShipment}
                    </p>
                  )}
                </div>
              </Popup>
              <Tooltip direction="top" offset={[0, -18]} opacity={0.9}>
                <span className="font-bold text-[11px]">{v.vehicleNumber}</span> ({v.speed} km/h)
              </Tooltip>
            </Marker>
          );
        })}

        {/* Hazard Events */}
        {hazards.map((h, idx) => {
          if (!h.latitude || !h.longitude) return null;
          const icon = h.type === 'LANDSLIDE' ? landslideIcon : h.type === 'FLOOD' ? floodIcon : blockIcon;
          return (
            <Marker
              key={`hazard-${h.id || idx}`}
              position={[h.latitude, h.longitude]}
              icon={icon}
            >
              <Popup>
                <div className="text-xs p-1 space-y-1">
                  <div className="flex items-center gap-1.5 text-red-400 font-bold">
                    <span>⚠️ {h.type} HAZARD</span>
                    <span className="text-[10px] px-1 rounded bg-red-900/60 text-red-200">{h.severity}</span>
                  </div>
                  <p className="text-slate-300">{h.description}</p>
                  <p className="text-[10px] text-slate-400">Source: {h.source || 'NDMA SACHET Telemetry'}</p>
                </div>
              </Popup>
            </Marker>
          );
        })}

        {/* Road Incident Reports */}
        {reports.map((r, idx) => {
          if (!r.latitude || !r.longitude) return null;
          return (
            <Marker
              key={`report-${r.id || idx}`}
              position={[r.latitude, r.longitude]}
              icon={landslideIcon}
            >
              <Popup>
                <div className="text-xs p-1 space-y-1">
                  <div className="flex items-center justify-between border-b border-slate-700 pb-1">
                    <span className="font-bold text-amber-400">Field Report: {r.type}</span>
                    <span className="text-[10px] px-1 rounded bg-amber-900/60 text-amber-200">{r.status}</span>
                  </div>
                  <p className="text-slate-200">{r.description}</p>
                  <p className="text-[10px] text-slate-400">Reported by: {r.reporter}</p>
                  {r.photoUrl && (
                    <img src={r.photoUrl} alt="Field damage" className="w-full h-24 object-cover rounded mt-1" />
                  )}
                </div>
              </Popup>
            </Marker>
          );
        })}
      </MapContainer>

      <div className="absolute top-4 right-4 z-10 flex items-center gap-1 rounded-xl bg-slate-900/95 backdrop-blur-md border border-slate-700 p-1 shadow-xl" role="group" aria-label="Map display mode">
        <button
          type="button"
          onClick={() => setMapMode('normal')}
          className={`p-2 rounded-lg transition ${mapMode === 'normal' ? 'bg-slate-700 text-white' : 'text-slate-400 hover:text-white hover:bg-slate-800'}`}
          title="Normal map"
          aria-label="Normal map"
          aria-pressed={mapMode === 'normal'}
        >
          <Sun className="w-4 h-4" />
        </button>
        <button
          type="button"
          onClick={() => setMapMode('terrain')}
          className={`p-2 rounded-lg transition ${mapMode === 'terrain' ? 'bg-emerald-600 text-white' : 'text-slate-400 hover:text-white hover:bg-slate-800'}`}
          title="Terrain map"
          aria-label="Terrain map"
          aria-pressed={mapMode === 'terrain'}
        >
          <Mountain className="w-4 h-4" />
        </button>
        <button
          type="button"
          onClick={() => setMapMode('3d')}
          className={`px-2 py-2 rounded-lg text-[10px] font-bold transition ${mapMode === '3d' ? 'bg-cyan-600 text-white' : 'text-slate-400 hover:text-white hover:bg-slate-800'}`}
          title="3D terrain view"
          aria-label="3D terrain view"
          aria-pressed={mapMode === '3d'}
        >
          3D
        </button>
        <button
          type="button"
          onClick={() => setMapMode('dark')}
          className={`p-2 rounded-lg transition ${mapMode === 'dark' ? 'bg-slate-700 text-white' : 'text-slate-400 hover:text-white hover:bg-slate-800'}`}
          title="Dark map"
          aria-label="Dark map"
          aria-pressed={mapMode === 'dark'}
        >
          <Moon className="w-4 h-4" />
        </button>
      </div>

      {mapMode === '3d' && (
        <div className="absolute left-4 top-4 z-10 rounded-lg border border-cyan-400/40 bg-cyan-950/85 px-2.5 py-1.5 text-[10px] font-bold uppercase tracking-wider text-cyan-200 shadow-lg">
          3D Terrain View
        </div>
      )}

      {/* Map Legend Overlay */}
      <div className="absolute bottom-4 right-4 z-10 bg-slate-900/90 backdrop-blur-md p-3 rounded-xl border border-slate-700 text-xs shadow-xl space-y-2 pointer-events-auto">
        <div className="font-bold text-slate-300 text-[11px] uppercase tracking-wider mb-1">
          Map Intelligence Legend
        </div>
        <div className="flex items-center gap-2">
          <span className="w-4 h-1.5 rounded-full bg-red-500"></span>
          <span className="text-slate-300">High Hazard Corridor</span>
        </div>
        <div className="flex items-center gap-2">
          <span className="w-4 h-1.5 rounded-full bg-emerald-500"></span>
          <span className="text-slate-300">Safe Disaster Bypass</span>
        </div>
        <div className="flex items-center gap-2">
          <span className="w-3 h-3 rounded-full bg-emerald-500 border border-white"></span>
          <span className="text-slate-300">Active GPS Vehicle</span>
        </div>
        <div className="flex items-center gap-2">
          <span className="w-3 h-3 rounded bg-red-600 border border-white"></span>
          <span className="text-slate-300">Landslide / Road Block</span>
        </div>
        <div className="flex items-center gap-2">
          <span className="w-3 h-3 rounded bg-sky-600 border border-white"></span>
          <span className="text-slate-300">Flood Inundation</span>
        </div>
      </div>
    </div>
  );
}
