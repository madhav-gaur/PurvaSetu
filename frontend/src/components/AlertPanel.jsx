import React from 'react';
import { 
  Bell, 
  AlertOctagon, 
  AlertTriangle, 
  Info, 
  CheckCheck, 
  Navigation, 
  CloudRain 
} from 'lucide-react';

export default function AlertPanel({ alerts = [], onAcknowledge }) {
  const getSeverityBadge = (severity) => {
    switch (severity) {
      case 'CRITICAL':
        return 'bg-red-500/20 text-red-300 border-red-500/30 animate-pulse';
      case 'HIGH':
        return 'bg-amber-500/20 text-amber-300 border-amber-500/30';
      case 'MEDIUM':
        return 'bg-yellow-500/20 text-yellow-300 border-yellow-500/30';
      default:
        return 'bg-blue-500/20 text-blue-300 border-blue-500/30';
    }
  };

  const getAlertIcon = (type, severity) => {
    if (severity === 'CRITICAL') return <AlertOctagon className="w-4 h-4 text-red-400" />;
    if (type === 'REROUTE') return <Navigation className="w-4 h-4 text-emerald-400" />;
    if (type === 'WEATHER_WARNING') return <CloudRain className="w-4 h-4 text-cyan-400" />;
    return <AlertTriangle className="w-4 h-4 text-amber-400" />;
  };

  return (
    <div className="p-5 rounded-2xl bg-[#0E131F]/90 border border-slate-800 shadow-xl flex flex-col h-full space-y-4">
      <div className="flex items-center justify-between border-b border-slate-800/80 pb-3">
        <div className="flex items-center gap-2">
          <div className="p-2 rounded-xl bg-red-500/10 text-red-400 border border-red-500/20">
            <Bell className="w-4 h-4" />
          </div>
          <div>
            <h3 className="text-xs font-bold uppercase tracking-wider text-slate-400">
              Live Alert Radar
            </h3>
            <p className="text-sm font-bold text-white">
              {alerts.length} Active Notice{alerts.length !== 1 ? 's' : ''}
            </p>
          </div>
        </div>
      </div>

      {alerts.length === 0 ? (
        <div className="flex-1 flex flex-col items-center justify-center p-6 text-center text-slate-500 space-y-2">
          <CheckCheck className="w-8 h-8 text-emerald-500/50" />
          <p className="text-xs font-semibold text-slate-400">All Corridors Clear</p>
          <p className="text-[11px]">No active critical hazards reported across North East corridors.</p>
        </div>
      ) : (
        <div className="space-y-2.5 overflow-y-auto max-h-[480px] pr-1">
          {alerts.map((alert) => (
            <div
              key={alert.id}
              className={`p-3.5 rounded-xl border transition-all duration-200 bg-slate-900/70 hover:bg-slate-900 ${
                alert.severity === 'CRITICAL' ? 'border-red-500/40 shadow-sm shadow-red-950/30' : 'border-slate-800'
              }`}
            >
              <div className="flex items-start justify-between gap-2 mb-1.5">
                <div className="flex items-center gap-2">
                  {getAlertIcon(alert.type, alert.severity)}
                  <span className={`text-[10px] font-mono px-2 py-0.5 rounded border font-semibold ${getSeverityBadge(alert.severity)}`}>
                    {alert.severity}
                  </span>
                  <span className="text-[10px] text-slate-400 font-mono">
                    {alert.type}
                  </span>
                </div>

                {onAcknowledge && !alert.acknowledged && (
                  <button
                    onClick={() => onAcknowledge(alert.id)}
                    className="text-[10px] text-slate-400 hover:text-emerald-400 font-medium px-2 py-0.5 rounded bg-slate-800 hover:bg-slate-700 transition"
                  >
                    Acknowledge
                  </button>
                )}
              </div>

              <h4 className="text-xs font-bold text-slate-100 leading-snug">
                {alert.title}
              </h4>
              <p className="text-[11px] text-slate-300 mt-1 leading-relaxed">
                {alert.message}
              </p>

              {alert.createdAt && (
                <div className="text-[10px] text-slate-400 mt-2 font-mono">
                  {new Date(alert.createdAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                </div>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
