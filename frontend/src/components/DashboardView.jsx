import React from 'react';
import {
  Truck,
  Package,
  AlertTriangle,
  Ban,
  BellRing,
  TrendingDown,
  ShieldCheck,
  Activity,
  ArrowUpRight
} from 'lucide-react';
import LeafletMap from './LeafletMap';
import AlertPanel from './AlertPanel';
import WeatherWidget from './WeatherWidget';

export default function DashboardView({
  summary = {},
  routes = {},
  weather = {},
  vehicles = [],
  reports = [],
  hazards = [],
  advisories = [],
  alerts = [],
  lastUpdated,
  routesLoading = false,
  onAcknowledgeAlert,
  onSelectWeatherLocation,
  onSelectVehicle,
  onViewRoutesTab
}) {
  const primaryRoute = routes?.recommendedRoute?.riskScore > 0.60
    ? routes?.alternativeRoutes?.[0] || routes?.recommendedRoute
    : routes?.recommendedRoute;

  const altRoute = routes?.recommendedRoute?.riskScore > 0.60
    ? routes?.recommendedRoute
    : routes?.alternativeRoutes?.[0];

  return (
    <div className="mx-auto max-w-[1700px] space-y-5 p-4 sm:p-6">
      <div className="flex flex-wrap items-end justify-between gap-2 border-b border-slate-800/80 pb-3">
        <div>
          <p className="text-[10px] font-bold uppercase tracking-[0.2em] text-cyan-400">Operations overview</p>
          <h2 className="mt-1 text-base font-bold text-white sm:text-lg">Regional Logistics Command</h2>
        </div>
        <p className="text-[10px] font-medium text-slate-500 sm:text-xs">
          Last updated: {lastUpdated ? lastUpdated.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) : 'Loading'}
        </p>
      </div>

      {/* KPI Cards Grid */}
      <div className="grid grid-cols-2 gap-3 md:grid-cols-3 lg:grid-cols-5">
        {/* Active Vehicles */}
        <div className="rounded-xl border border-cyan-500/20 bg-[#0E131F]/90 p-3 shadow-lg transition hover:border-cyan-400/40 sm:p-4">
          <div className="mb-1.5 flex items-center justify-between text-slate-400">
            <span className="text-xs font-semibold">Active Fleet</span>
            <div className="rounded-lg bg-cyan-500/10 p-1.5 text-cyan-400">
              <Truck className="w-4 h-4" />
            </div>
          </div>
          <div className="text-xl font-extrabold text-cyan-100 sm:text-2xl">
            {summary.activeVehiclesCount ?? vehicles.length}
          </div>
          <div className="text-[10px] text-emerald-400 font-medium flex items-center gap-1 mt-1">
            <span className="w-1.5 h-1.5 rounded-full bg-emerald-400"></span>
            100% GPS Telemetry Active
          </div>
        </div>

        {/* Active Shipments */}
        <div className="rounded-xl border border-cyan-500/20 bg-[#0E131F]/90 p-3 shadow-lg transition hover:border-cyan-400/40 sm:p-4">
          <div className="mb-1.5 flex items-center justify-between text-slate-400">
            <span className="text-xs font-semibold">Consignments</span>
            <div className="rounded-lg bg-cyan-500/10 p-1.5 text-cyan-400">
              <Package className="w-4 h-4" />
            </div>
          </div>
          <div className="text-xl font-extrabold text-cyan-100 sm:text-2xl">
            {summary.activeShipmentsCount ?? 4}
          </div>
          <div className="text-[10px] text-amber-400 font-medium flex items-center gap-1 mt-1">
            <span className="w-1.5 h-1.5 rounded-full bg-amber-400"></span>
            {summary.criticalShipmentsCount ?? 1} Critical Consignment
          </div>
        </div>

        {/* High Risk Routes */}
        <div className="rounded-xl border border-amber-500/25 bg-[#0E131F]/90 p-3 shadow-lg transition hover:border-amber-400/50 sm:p-4">
          <div className="flex items-center justify-between text-slate-400 mb-2">
            <span className="text-xs font-semibold">High Hazard Routes</span>
            <div className="rounded-lg bg-amber-500/10 p-1.5 text-amber-400">
              <AlertTriangle className="w-4 h-4" />
            </div>
          </div>
          <div className="text-xl font-extrabold text-amber-300 sm:text-2xl">
            {summary.highRiskRoutesCount ?? 2}
          </div>
          <div className="text-[10px] text-amber-400 font-medium flex items-center gap-1 mt-1">
            NH-6 & NH-10 Monsoon Alert
          </div>
        </div>

        {/* Blocked Corridors */}
        <div className="rounded-xl border border-red-500/25 bg-[#0E131F]/90 p-3 shadow-lg transition hover:border-red-400/50 sm:p-4">
          <div className="flex items-center justify-between text-slate-400 mb-2">
            <span className="text-xs font-semibold">Passage Blockages</span>
            <div className="rounded-lg bg-red-500/10 p-1.5 text-red-400">
              <Ban className="w-4 h-4" />
            </div>
          </div>
          <div className="text-xl font-extrabold text-red-300 sm:text-2xl">
            {summary.blockedRoadsCount ?? 1}
          </div>
          <div className="text-[10px] text-rose-400 font-medium flex items-center gap-1 mt-1">
            Sonapur Mudslide Active
          </div>
        </div>

        {/* Critical Alerts */}
        <div className="rounded-xl border border-red-500/35 bg-[#0E131F]/90 p-3 shadow-lg transition hover:border-red-400/60 sm:p-4">
          <div className="flex items-center justify-between text-slate-400 mb-2">
            <span className="text-xs font-semibold">Critical Notices</span>
            <div className="rounded-lg bg-red-500/10 p-1.5 text-red-400">
              <BellRing className="w-4 h-4" />
            </div>
          </div>
          <div className="text-xl font-extrabold text-red-400 sm:text-2xl">
            {summary.criticalAlertsCount ?? alerts.length}
          </div>
          <div className="text-[10px] text-red-400 font-medium flex items-center gap-1 mt-1">
            Immediate Attention Required
          </div>
        </div>
      </div>

      {/* Main Command Map & Alert Feed */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Large Map Visualizer (2 Cols) */}
        <div className="lg:col-span-2 space-y-4">
          <div className="flex flex-wrap items-center justify-between gap-3 rounded-xl border border-cyan-500/25 bg-slate-900/40 px-3 py-2.5">
            <div className="flex items-center gap-2">
              <span className="w-2.5 h-2.5 rounded-full bg-emerald-400 animate-pulse"></span>
              <h2 className="text-xs font-bold uppercase tracking-wider text-white sm:text-sm">
                PurvaSetu Real-Time Disaster & Routing Command Map
              </h2>
            </div>
            <div className="flex items-center gap-2">
              {routesLoading && <span className="flex items-center gap-1.5 text-[10px] font-semibold text-cyan-300"><span className="h-2 w-2 animate-pulse rounded-full bg-cyan-400" />Updating route</span>}
              <button
                onClick={onViewRoutesTab}
                className="flex items-center gap-1 rounded-lg border border-cyan-500/25 bg-cyan-500/10 px-2.5 py-1.5 text-[10px] font-semibold text-cyan-300 hover:bg-cyan-500/20 sm:text-xs"
              >
                <span>Explore Multi-Route Risk Engine</span>
                <ArrowUpRight className="w-3.5 h-3.5" />
              </button>
            </div>
          </div>

          <LeafletMap
            primaryRoute={routes?.recommendedRoute}
            alternativeRoute={routes?.alternativeRoutes?.[0]}
            vehicles={vehicles}
            hazards={hazards}
            reports={reports}
            originName={routes?.origin}
            destinationName={routes?.destination}
            onSelectVehicle={onSelectVehicle}
            height="550px"
            loading={routesLoading}
          />
        </div>

        {/* Right Side Alert Panel & Weather Widget (1 Col) */}
        <div className="space-y-6 flex flex-col justify-between">
          <AlertPanel
            alerts={alerts}
            onAcknowledge={onAcknowledgeAlert}
          />
        </div>
      </div>

      {/* Full-width weather telemetry */}
      <WeatherWidget
        weather={weather}
        onSelectLocation={onSelectWeatherLocation}
      />

      {/* Bottom Operational Panels Grid */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Critical Shipments Table */}
        <div className="p-5 rounded-2xl bg-[#0E131F]/90 border border-slate-800 shadow-xl space-y-4">
          <div className="flex items-center justify-between border-b border-slate-800 pb-3">
            <div className="flex items-center gap-2">
              <div className="p-2 rounded-xl bg-red-500/10 text-red-400">
                <Package className="w-4 h-4" />
              </div>
              <h3 className="text-xs font-bold uppercase tracking-wider text-slate-300">
                Priority Consignments
              </h3>
            </div>
            <span className="text-[10px] font-mono px-2 py-0.5 rounded bg-red-500/20 text-red-300 font-bold border border-red-500/30">
              LIFE-SAVING FIRST
            </span>
          </div>

          <div className="space-y-3">
            {(summary.criticalShipments || []).length === 0 ? (
              <p className="text-xs text-slate-500">No critical shipments active.</p>
            ) : (
              (summary.criticalShipments || []).map((s) => (
                <div key={s.id} className="p-3 rounded-xl bg-slate-900/60 border border-slate-800/80 space-y-1.5">
                  <div className="flex items-center justify-between">
                    <span className="text-xs font-bold text-white">{s.trackingNumber}</span>
                    <span className="text-[10px] font-mono px-1.5 py-0.5 rounded bg-red-900/40 text-red-200 border border-red-700/50">
                      {s.priority}
                    </span>
                  </div>
                  <p className="text-xs text-slate-300 truncate">{s.cargoType}</p>
                  <div className="flex items-center justify-between text-[11px] text-slate-400 pt-1 border-t border-slate-800">
                    <span>{s.source?.split(',')[0]} → {s.destination?.split(',')[0]}</span>
                    <span className="text-emerald-400 font-medium">{s.status}</span>
                  </div>
                </div>
              ))
            )}
          </div>
        </div>

        {/* Recent Road Incident Reports */}
        <div className="p-5 rounded-2xl bg-[#0E131F]/90 border border-slate-800 shadow-xl space-y-4">
          <div className="flex items-center justify-between border-b border-slate-800 pb-3">
            <div className="flex items-center gap-2">
              <div className="p-2 rounded-xl bg-amber-500/10 text-amber-400">
                <AlertTriangle className="w-4 h-4" />
              </div>
              <h3 className="text-xs font-bold uppercase tracking-wider text-slate-300">
                Field Road Reports
              </h3>
            </div>
            <span className="text-[10px] font-mono px-2 py-0.5 rounded bg-slate-800 text-slate-300">
              VERIFIED HAZARDS
            </span>
          </div>

          <div className="space-y-3">
            {(summary.recentReports || []).slice(0, 3).map((r) => (
              <div key={r.id} className="p-3 rounded-xl bg-slate-900/60 border border-slate-800/80 space-y-1">
                <div className="flex items-center justify-between text-xs">
                  <span className="font-bold text-slate-200">{r.type}</span>
                  <span className="text-[10px] text-amber-400 font-semibold">{r.severity}</span>
                </div>
                <p className="text-[11px] text-slate-400 leading-snug line-clamp-2">{r.description}</p>
                <div className="text-[10px] text-slate-500 flex justify-between pt-1">
                  <span>By: {r.reporter}</span>
                  <span className="text-emerald-400">{r.status}</span>
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* AI Route Recommendation Card */}
        <div className="p-5 rounded-2xl bg-gradient-to-br from-[#0E131F] to-emerald-950/20 border border-emerald-500/30 shadow-xl space-y-4">
          <div className="flex items-center justify-between border-b border-slate-800 pb-3">
            <div className="flex items-center gap-2">
              <div className="p-2 rounded-xl bg-emerald-500/10 text-emerald-400 border border-emerald-500/20">
                <ShieldCheck className="w-4 h-4" />
              </div>
              <h3 className="text-xs font-bold uppercase tracking-wider text-emerald-300">
                AI Pathfinding Recommendation
              </h3>
            </div>
            <span className="text-[10px] font-mono px-2 py-0.5 rounded bg-emerald-500/20 text-emerald-300 font-bold">
              OPTIMIZED
            </span>
          </div>

          <div className="space-y-3 text-xs">
            <div className="p-3 rounded-xl bg-slate-900/70 border border-slate-800 space-y-2">
              <div className="flex items-center justify-between">
                <span className="font-bold text-white">Recommended Corridor:</span>
                <span className="text-emerald-400 font-semibold">
                  {routes?.recommendedRoute?.routeName || 'Northern Disaster Bypass (NH-27)'}
                </span>
              </div>
              <p className="text-slate-300 text-[11px] leading-relaxed">
                {routes?.recommendationReason || 'Optimal safety corridor: avoids high-landslide active zones in Meghalaya ghats for critical life-saving cargo.'}
              </p>
            </div>

            <div className="grid grid-cols-2 gap-2 text-[11px]">
              <div className="p-2.5 rounded-lg bg-slate-900/60 border border-slate-800">
                <span className="text-slate-400">Risk Reduction:</span>
                <p className="text-sm font-bold text-emerald-400 mt-0.5">
                  {routes?.riskDeltaPercent ? `-${routes.riskDeltaPercent}% Hazard` : '-62% Hazard'}
                </p>
              </div>
              <div className="p-2.5 rounded-lg bg-slate-900/60 border border-slate-800">
                <span className="text-slate-400">Transit Delta:</span>
                <p className="text-sm font-bold text-cyan-300 mt-0.5">
                  {routes?.timeDeltaMinutes ? `+${routes.timeDeltaMinutes} mins` : '+45 mins safe detour'}
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
