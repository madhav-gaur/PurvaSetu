import React, { useEffect, useState } from 'react';
import {
  Route as RouteIcon,
  ShieldCheck,
  AlertTriangle,
  ArrowRight,
  CheckCircle2,
  Navigation,
  Clock,
  Compass,
  Sparkles,
  Zap
} from 'lucide-react';
import LeafletMap from './LeafletMap';
import CustomSelect from './CustomSelect';
import { calculateRoutes, rerouteShipment } from '../services/api';
import { DEFAULT_DESTINATION, DEFAULT_ORIGIN, ROUTE_LOCATIONS } from '../constants/routeLocations';

export default function RouteComparisonView({
  routeData,
  onRoutesCalculated,
  routeSelection,
  onRouteSelectionChange,
  vehicles = [],
  shipments = []
}) {
  const [loading, setLoading] = useState(false);
  const [rerouteStatus, setRerouteStatus] = useState(null);

  // Selected points
  const [startPoint, setStartPoint] = useState(routeSelection?.origin || DEFAULT_ORIGIN);
  const [destPoint, setDestPoint] = useState(routeSelection?.destination || DEFAULT_DESTINATION);
  const [priority, setPriority] = useState('CRITICAL');

  useEffect(() => {
    if (routeSelection?.origin) setStartPoint(routeSelection.origin);
    if (routeSelection?.destination) setDestPoint(routeSelection.destination);
  }, [routeSelection]);

  const handleCalculate = async () => {
    try {
      setLoading(true);
      setRerouteStatus(null);
      const res = await calculateRoutes({
        startLatitude: startPoint.lat,
        startLongitude: startPoint.lng,
        destinationLatitude: destPoint.lat,
        destinationLongitude: destPoint.lng,
        originName: startPoint.name,
        destinationName: destPoint.name,
        priority: priority,
      });
      if (onRoutesCalculated) onRoutesCalculated(res);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleReroute = async (targetRouteId) => {
    try {
      setLoading(true);
      const criticalShipment = shipments.find(s => s.priority === 'CRITICAL') || shipments[0];
      const res = await rerouteShipment({
        shipmentId: criticalShipment ? criticalShipment.id : 1,
        targetRouteId: targetRouteId || routeData?.recommendedRoute?.id,
        reason: 'Operator accepted AI disaster bypass safety recommendation.'
      });
      setRerouteStatus(`Dynamic Reroute Successfully Dispatched! Vehicle directed to ${res.routeName}. Alert broadcasted.`);
    } catch (err) {
      console.error(err);
      setRerouteStatus(`Reroute update failed: ${err.message}`);
    } finally {
      setLoading(false);
    }
  };

  const routeA = routeData?.recommendedRoute?.riskScore > 0.60
    ? routeData?.alternativeRoutes?.[0] || routeData?.recommendedRoute
    : routeData?.recommendedRoute;

  const routeB = routeData?.recommendedRoute?.riskScore > 0.60
    ? routeData?.recommendedRoute
    : routeData?.alternativeRoutes?.[0];

  return (
    <div className="p-6 space-y-6 max-w-[1700px] mx-auto">
      {/* Header & Controls */}
      <div className="p-6 rounded-2xl bg-[#0E131F]/90 border border-slate-800 shadow-xl space-y-4">
        <div className="flex flex-col lg:flex-row lg:items-center justify-between gap-4 border-b border-slate-800 pb-4">
          <div>
            <h2 className="text-base font-bold text-white flex items-center gap-2">
              Smart Route Scoring & Dynamic Rerouting Engine
              <span className="text-[10px] uppercase font-mono px-2 py-0.5 rounded bg-emerald-500/20 text-emerald-300 border border-emerald-500/30">
                Core Differentiator
              </span>
            </h2>
            <p className="text-xs text-slate-400">
              "Don't just find the shortest route. Find the safest and most accessible route."
            </p>
          </div>

          <button
            disabled={loading}
            onClick={handleCalculate}
            className="px-5 py-2.5 rounded-xl bg-gradient-to-r from-emerald-600 to-teal-600 hover:from-emerald-500 hover:to-teal-500 text-white font-bold text-xs shadow-lg shadow-emerald-950/40 flex items-center gap-2 transition"
          >
            <Zap className="w-4 h-4 text-emerald-300" />
            <span>{loading ? 'Evaluating Corridors...' : 'Calculate Safe Routes'}</span>
          </button>
        </div>

        {/* Input selectors */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div>
            <label className="block text-xs font-semibold text-slate-400 mb-1.5">Origin Terminal</label>
            <CustomSelect
              value={startPoint.name}
              onChange={(value) => {
                const loc = ROUTE_LOCATIONS.find(l => l.name === value);
                if (loc) {
                  setStartPoint(loc);
                  onRouteSelectionChange?.(loc, destPoint);
                }
              }}
              options={ROUTE_LOCATIONS.map(l => ({ value: l.name, label: l.name }))}
            />
          </div>

          <div>
            <label className="block text-xs font-semibold text-slate-400 mb-1.5">Destination Terminal</label>
            <CustomSelect
              value={destPoint.name}
              onChange={(value) => {
                const loc = ROUTE_LOCATIONS.find(l => l.name === value);
                if (loc) {
                  setDestPoint(loc);
                  onRouteSelectionChange?.(startPoint, loc);
                }
              }}
              options={ROUTE_LOCATIONS.filter(l => l.name !== startPoint.name).map(l => ({ value: l.name, label: l.name }))}
            />
          </div>

          <div>
            <label className="block text-xs font-semibold text-slate-400 mb-1.5">
              Consignment Priority (Safety Weight Factor)
            </label>
            <CustomSelect
              value={priority}
              onChange={setPriority}
              options={[
                { value: 'CRITICAL', label: 'CRITICAL (Medical / Vaccines - Safety 3.5x Weight)' },
                { value: 'HIGH', label: 'HIGH (Relief Grain / Fuel - Safety 2.2x Weight)' },
                { value: 'MEDIUM', label: 'MEDIUM (Commercial Cargo - Balanced)' },
                { value: 'LOW', label: 'LOW (Non-Urgent Bulk - Distance Prioritized)' },
              ]}
            />
          </div>
        </div>
      </div>

      {rerouteStatus && (
        <div className="p-4 rounded-2xl bg-emerald-500/10 border border-emerald-500/30 text-emerald-300 text-xs flex items-center gap-2 font-medium">
          <CheckCircle2 className="w-5 h-5 text-emerald-400 shrink-0" />
          <span>{rerouteStatus}</span>
        </div>
      )}

      {/* Side-by-Side Corridor Scoring Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {/* Route A Card */}
        <div className="p-5 rounded-2xl bg-[#0E131F] border border-slate-800 shadow-xl space-y-4">
          <div className="flex items-center justify-between border-b border-slate-800 pb-3">
            <div>
              <span className="text-[10px] font-mono uppercase tracking-wider text-slate-400">Option 1</span>
              <h3 className="text-sm font-bold text-white">
                {routeA?.routeName || 'Primary Highway Corridor (NH-6)'}
              </h3>
            </div>
            <span className={`text-[10px] font-mono font-bold px-2 py-0.5 rounded border ${(routeA?.riskScore || 0.78) > 0.6
              ? 'bg-red-500/20 text-red-300 border-red-500/40'
              : 'bg-emerald-500/20 text-emerald-300 border-emerald-500/40'
              }`}>
              {Math.round((routeA?.riskScore || 0.78) * 100)}% RISK ({(routeA?.riskScore || 0.78) > 0.6 ? 'HIGH RISK' : 'SAFE'})
            </span>
          </div>

          <div className="grid grid-cols-3 gap-2 text-center text-xs">
            <div className="p-2.5 rounded-xl bg-slate-900/60 border border-slate-800">
              <span className="text-[10px] text-slate-400">Distance</span>
              <p className="text-sm font-bold text-white mt-0.5">{Math.round(routeA?.distance || 298)} km</p>
            </div>
            <div className="p-2.5 rounded-xl bg-slate-900/60 border border-slate-800">
              <span className="text-[10px] text-slate-400">Est. Time</span>
              <p className="text-sm font-bold text-white mt-0.5">{routeA?.formattedDuration || '6h 15m'}</p>
            </div>
            <div className="p-2.5 rounded-xl bg-slate-900/60 border border-slate-800">
              <span className="text-[10px] text-slate-400">Accessibility</span>
              <p className="text-sm font-bold text-amber-400 mt-0.5">
                {Math.round((routeA?.accessibilityScore || 0.22) * 100)}%
              </p>
            </div>
          </div>

          {/* Detailed Hazard Factor Breakdown */}
          <div className="p-3.5 rounded-xl bg-slate-900/40 border border-slate-800/80 space-y-2 text-xs">
            <span className="text-[10px] font-bold uppercase tracking-wider text-slate-400">Corridor Risk Breakdown</span>
            <div className="space-y-1.5 text-[11px]">
              <div className="flex justify-between text-slate-300">
                <span>Landslide & Soil Saturation:</span>
                <span className="text-red-400 font-bold">{Math.round((routeA?.landslideRisk || 0.85) * 100)}%</span>
              </div>
              <div className="flex justify-between text-slate-300">
                <span>River Inundation Flood Risk:</span>
                <span className="text-amber-400 font-bold">{Math.round((routeA?.floodRisk || 0.65) * 100)}%</span>
              </div>
              <div className="flex justify-between text-slate-300">
                <span>Road Surface Obstruction:</span>
                <span className="text-red-400 font-bold">{Math.round((routeA?.roadDisruptionRisk || 0.74) * 100)}%</span>
              </div>
            </div>
          </div>

          <div className="p-3 rounded-xl bg-red-950/20 border border-red-900/40 text-red-300 text-xs">
            <strong>Safety Warning:</strong> {routeA?.safetyRecommendation || 'Sonapur ghat sector vulnerable to active mudslides during monsoon precipitation.'}
          </div>
        </div>

        {/* Route B Card (Safe Bypass) */}
        <div className="p-5 rounded-2xl bg-[#0E131F] border border-emerald-500/40 shadow-xl space-y-4 relative">
          <div className="absolute top-4 right-4">
            <span className="text-[10px] font-mono font-bold px-2 py-1 rounded-full bg-emerald-500/20 text-emerald-300 border border-emerald-500/50 flex items-center gap-1">
              <ShieldCheck className="w-3.5 h-3.5 text-emerald-400" />
              RECOMMENDED SAFE BYPASS
            </span>
          </div>

          <div className="border-b border-slate-800 pb-3">
            <span className="text-[10px] font-mono uppercase tracking-wider text-emerald-400">Option 2</span>
            <h3 className="text-sm font-bold text-white">
              {routeB?.routeName || 'Alternative Disaster Bypass (NH-27 Lumding)'}
            </h3>
          </div>

          <div className="grid grid-cols-3 gap-2 text-center text-xs">
            <div className="p-2.5 rounded-xl bg-slate-900/60 border border-slate-800">
              <span className="text-[10px] text-slate-400">Distance</span>
              <p className="text-sm font-bold text-white mt-0.5">{Math.round(routeB?.distance || 342)} km</p>
            </div>
            <div className="p-2.5 rounded-xl bg-slate-900/60 border border-slate-800">
              <span className="text-[10px] text-slate-400">Est. Time</span>
              <p className="text-sm font-bold text-white mt-0.5">{routeB?.formattedDuration || '7h 05m'}</p>
            </div>
            <div className="p-2.5 rounded-xl bg-slate-900/60 border border-slate-800">
              <span className="text-[10px] text-slate-400">Accessibility</span>
              <p className="text-sm font-bold text-emerald-400 mt-0.5">
                {Math.round((routeB?.accessibilityScore || 0.78) * 100)}%
              </p>
            </div>
          </div>

          {/* Detailed Hazard Factor Breakdown */}
          <div className="p-3.5 rounded-xl bg-slate-900/40 border border-slate-800/80 space-y-2 text-xs">
            <span className="text-[10px] font-bold uppercase tracking-wider text-slate-400">Corridor Risk Breakdown</span>
            <div className="space-y-1.5 text-[11px]">
              <div className="flex justify-between text-slate-300">
                <span>Landslide & Soil Saturation:</span>
                <span className="text-emerald-400 font-bold">{Math.round((routeB?.landslideRisk || 0.15) * 100)}%</span>
              </div>
              <div className="flex justify-between text-slate-300">
                <span>River Inundation Flood Risk:</span>
                <span className="text-emerald-400 font-bold">{Math.round((routeB?.floodRisk || 0.20) * 100)}%</span>
              </div>
              <div className="flex justify-between text-slate-300">
                <span>Road Surface Obstruction:</span>
                <span className="text-emerald-400 font-bold">{Math.round((routeB?.roadDisruptionRisk || 0.18) * 100)}%</span>
              </div>
            </div>
          </div>

          <div className="p-3 rounded-xl bg-emerald-950/20 border border-emerald-900/40 text-emerald-300 text-xs">
            <strong>Decision Rationale:</strong> Lower predicted hazard and disruption risk. Safely bypasses unstable mountain terrain with all-weather road bed.
          </div>

          {/* One-Click Reroute Action */}
          <button
            disabled={loading}
            onClick={() => handleReroute(routeB?.id)}
            className="w-full py-2.5 px-4 rounded-xl bg-gradient-to-r from-emerald-600 to-teal-600 hover:from-emerald-500 hover:to-teal-500 text-white font-bold text-xs shadow-lg shadow-emerald-950/50 flex items-center justify-center gap-2 transition"
          >
            <Navigation className="w-4 h-4" />
            <span>DISPATCH & REROUTE TO SAFE BYPASS</span>
          </button>
        </div>
      </div>

      {/* Map View of Routes */}
      <div className="space-y-3">
        <h3 className="text-xs font-bold text-slate-300 uppercase tracking-wider">
          Visual Multi-Corridor Topology Overlay
        </h3>
        <LeafletMap
          primaryRoute={routeA}
          alternativeRoute={routeB}
          originName={startPoint.name}
          destinationName={destPoint.name}
          vehicles={vehicles}
          height="500px"
        />
      </div>
    </div>
  );
}
