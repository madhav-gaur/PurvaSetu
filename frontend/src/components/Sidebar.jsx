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

export default function Sidebar({ currentTab, setTab, counts = {}, collapsed = false, onToggle }) {
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
    <aside className={`fixed left-0 top-24 bottom-0 z-[90] bg-[#0A0D14] border-r border-slate-800/80 flex flex-col justify-between p-3 select-none transition-[width] duration-200 ${collapsed ? 'w-16' : 'w-64'}`}>
      <div className="space-y-1">
        <div className={`flex items-center mb-2 ${collapsed ? 'justify-center' : 'justify-between'}`}>
          {!collapsed && (
            <div className="px-3 py-2 text-[11px] font-bold uppercase tracking-wider text-slate-400">
              Operational Modules
            </div>
          )}
          <button
            type="button"
            onClick={onToggle}
            className="p-2 rounded-lg text-slate-400 hover:text-white hover:bg-slate-800/70 transition"
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
            <button
              key={item.id}
              onClick={() => setTab(item.id)}
              title={collapsed ? item.label : undefined}
              className={`w-full flex items-center ${collapsed ? 'justify-center' : 'justify-between'} px-3 py-2.5 rounded-xl text-xs font-semibold transition-all duration-150 ${isActive
                ? 'bg-emerald-500/15 text-emerald-400 border border-emerald-500/30 shadow-sm shadow-emerald-950/40'
                : item.highlight
                  ? 'text-amber-400 hover:bg-amber-500/10 border border-amber-500/20'
                  : 'text-slate-400 hover:text-slate-200 hover:bg-slate-800/50'
                }`}
            >
              <div className={`flex items-center ${collapsed ? 'justify-center' : 'gap-3'}`}>
                <Icon className={`w-4 h-4 ${isActive ? 'text-emerald-400' : item.highlight ? 'text-amber-400' : 'text-slate-400'}`} />
                {!collapsed && <span>{item.label}</span>}
              </div>

              {!collapsed && item.count !== undefined && item.count > 0 && (
                <span className={`text-[10px] font-mono px-1.5 py-0.5 rounded-full ${item.badgeColor || 'bg-slate-800 text-slate-300'}`}>
                  {item.count}
                </span>
              )}
            </button>
          );
        })}
      </div>

      {/* Regional Disclaimer footer */}
      {!collapsed && <div className="p-3 rounded-xl bg-slate-900/60 border border-slate-800/80 text-[11px] text-slate-400 leading-relaxed">
        <div className="font-semibold text-slate-300 flex items-center gap-1 mb-1">
          <span className="w-1.5 h-1.5 rounded-full bg-emerald-400"></span>
          NER Disaster Watch
        </div>
        <p className="text-[10px] text-slate-400">
          Covering critical relief corridors: Guwahati, Silchar, Dibang Valley, Anjaw & Darjeeling.
        </p>
      </div>}
    </aside>
  );
}
