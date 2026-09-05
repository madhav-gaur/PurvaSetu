import React from 'react';
import {
  LayoutDashboard,
  Map,
  Truck,
  Package,
  FileWarning,
  AlertTriangle,
  Route,
  Bell,
  ScrollText,
  BarChart3,
  SlidersHorizontal,
  PanelLeftClose,
  PanelLeftOpen
} from 'lucide-react';

export default function Sidebar({ currentTab, setTab, counts = {}, collapsed = false, onToggle, mobileOpen = false, onMobileClose }) {
  const showLabels = !collapsed || mobileOpen;
  const navItems = [
    { id: 'dashboard', label: 'Dashboard', icon: LayoutDashboard },
    { id: 'map', label: 'Live Map', icon: Map },
    { id: 'vehicles', label: 'Vehicles Fleet', icon: Truck, count: counts.activeVehicles },
    { id: 'shipments', label: 'Shipments', icon: Package, count: counts.criticalShipments, badgeColor: 'bg-red-500/20 text-red-300' },
    { id: 'reports', label: 'Road Reports', icon: FileWarning, count: counts.pendingReports },
    { id: 'hazards', label: 'Risk & Hazards', icon: AlertTriangle },
    { id: 'routes', label: 'Smart Routes', icon: Route },
    { id: 'advisories', label: 'Advisories', icon: ScrollText, count: counts.activeAdvisories },
    { id: 'analytics', label: 'Analytics', icon: BarChart3 },
    { id: 'simulation', label: 'Simulation Lab', icon: SlidersHorizontal, highlight: true }
  ];

  return (
    <>
      {mobileOpen && (
        <button
          type="button"
          onClick={onMobileClose}
          className="fixed inset-0 top-16 z-[80] bg-black/50 lg:hidden"
          aria-label="Close navigation menu"
        />
      )}
      <aside className={`fixed left-0 top-16 bottom-0 z-[90] flex w-72 flex-col justify-between border-r border-slate-800/80 bg-[#0A0D14] p-2 select-none transition-transform duration-200 lg:top-36 lg:p-3 lg:transition-[width] lg:translate-x-0 ${mobileOpen ? 'translate-x-0' : '-translate-x-full'} ${collapsed ? 'lg:w-16' : 'lg:w-64'}`}>
        <div className="space-y-1">
          <div className={`mb-2 flex items-center ${showLabels ? 'justify-between' : 'justify-center'}`}>
            {showLabels && (
              <div className="px-3 py-2 text-[11px] font-bold uppercase tracking-wider text-slate-400">
                Operational Modules
              </div>
            )}
            <button
              type="button"
              onClick={onToggle}
              className="hidden rounded-lg p-2 text-slate-400 transition hover:bg-slate-800/70 hover:text-white lg:block"
              title={collapsed ? 'Expand sidebar' : 'Collapse sidebar'}
              aria-label={collapsed ? 'Expand sidebar' : 'Collapse sidebar'}
            >
              {collapsed ? <PanelLeftOpen className="w-4 h-4" /> : <PanelLeftClose className="w-4 h-4" />}
            </button>
          </div>

          {navItems.map((item) => {
            const Icon = item.icon;
            const isActive = currentTab === item.id;
            return (
              <div key={item.id} className="group/sidebar-item relative">
                <button
                  onClick={() => {
                    setTab(item.id);
                    onMobileClose?.();
                  }}
                  title={showLabels ? undefined : item.label}
                  className={`w-full flex items-center ${showLabels ? 'justify-between' : 'justify-center'} rounded-xl px-3 py-2.5 text-xs font-semibold transition-all duration-150 ${isActive
                    ? 'bg-emerald-500/15 text-emerald-400 border border-emerald-500/30 shadow-sm shadow-emerald-950/40'
                    : item.highlight
                      ? 'text-amber-400 hover:bg-amber-500/10 border border-amber-500/20'
                      : 'text-slate-400 hover:text-slate-200 hover:bg-slate-800/50'
                    }`}
                >
                  <div className={`flex items-center ${showLabels ? 'gap-3' : 'justify-center'}`}>
                    <Icon className={`w-4 h-4 ${isActive ? 'text-emerald-400' : item.highlight ? 'text-amber-400' : 'text-slate-400'}`} />
                    {showLabels && <span>{item.label}</span>}
                  </div>

                  {showLabels && item.count !== undefined && item.count > 0 && (
                    <span className={`text-[10px] font-mono px-1.5 py-0.5 rounded-full ${item.badgeColor || 'bg-slate-800 text-slate-300'}`}>
                      {item.count}
                    </span>
                  )}
                </button>
                {!showLabels && (
                  <span className="pointer-events-none absolute left-full top-1/2 z-[120] ml-3 -translate-y-1/2 whitespace-nowrap rounded-lg border border-cyan-500/30 bg-[#0E131F] px-3 py-2 text-xs font-semibold text-cyan-200 opacity-0 shadow-xl transition-opacity group-hover/sidebar-item:opacity-100">
                    {item.label}
                  </span>
                )}
              </div>
            );
          })}
        </div>

        {/* Regional Disclaimer footer */}
        {showLabels && <div className="rounded-xl border border-slate-800/80 bg-slate-900/60 p-3 text-[11px] leading-relaxed text-slate-400">
          <div className="font-semibold text-slate-300 flex items-center gap-1 mb-1">
            <span className="w-1.5 h-1.5 rounded-full bg-emerald-400"></span>
            PurvaSetu Disaster Watch
          </div>
          <p className="text-[10px] text-slate-400">
            Covering critical relief corridors: Guwahati, Silchar, Dibang Valley, Anjaw & Darjeeling.
          </p>
        </div>}
      </aside>
    </>
  );
}
