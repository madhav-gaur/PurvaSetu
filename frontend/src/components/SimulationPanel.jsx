import React, { useState } from 'react';
import { 
  SlidersHorizontal, 
  CloudRain, 
  Mountain, 
  Waves, 
  AlertOctagon, 
  Clock, 
  FileEdit, 
  Sparkles, 
  CheckCircle2 
} from 'lucide-react';
import { triggerSimulation, createRoadReport } from '../services/api';

export default function SimulationPanel({ onScenarioTriggered, onClose }) {
  const [loading, setLoading] = useState(false);
  const [lastAction, setLastAction] = useState(null);

  const scenarios = [
    {
      id: 'HEAVY_RAIN',
      title: 'Simulate Heavy Rain (110mm/24h)',
      desc: 'Injects severe cloudburst telemetry over Meghalaya ghats. Elevates landslide and soil saturation index to 82%.',
      icon: CloudRain,
      color: 'from-blue-600/20 to-cyan-600/20 text-cyan-400 border-cyan-500/30'
    },
    {
      id: 'LANDSLIDE_BLOCKAGE',
      title: 'Simulate Landslide on NH-6',
      desc: 'Simulates active 40m mudslide blocking NH-6 at Sonapur. Forces route risk into CRITICAL and triggers safer bypass advice.',
      icon: Mountain,
      color: 'from-rose-600/20 to-red-600/20 text-rose-400 border-rose-500/30'
    },
    {
      id: 'FLOOD_SURGE',
      title: 'Simulate Flash Flood Inundation',
      desc: 'Simulates riverbank overflow on lower Barak valley plains. Water depth 0.6m over highway.',
      icon: Waves,
      color: 'from-indigo-600/20 to-sky-600/20 text-sky-400 border-sky-500/30'
    },
    {
      id: 'ROAD_CLOSURE',
      title: 'Simulate Official Highway Closure',
      desc: 'Injects official MSDMA / ASDMA advisory closing NH-6 for heavy commercial consignments.',
      icon: AlertOctagon,
      color: 'from-amber-600/20 to-orange-600/20 text-amber-400 border-amber-500/30'
    }
  ];

  const handleTrigger = async (scenarioId) => {
    try {
      setLoading(true);
      await triggerSimulation(scenarioId);
      setLastAction(`Successfully injected disaster scenario: ${scenarioId}`);
      if (onScenarioTriggered) onScenarioTriggered(scenarioId);
    } catch (err) {
      console.error(err);
      setLastAction(`Error triggering scenario: ${err.message}`);
    } finally {
      setLoading(false);
    }
  };

  const handleSimulateReport = async () => {
    try {
      setLoading(true);
      await createRoadReport({
        reporter: 'Simulated Driver Report',
        latitude: 25.1200,
        longitude: 92.3800,
        type: 'LANDSLIDE',
        severity: 'CRITICAL',
        description: 'Simulated incident: Fallen boulders and mud slurry blocking right carriageway.',
        status: 'PENDING'
      });
      setLastAction('Submitted simulated crowdsourced road report!');
      if (onScenarioTriggered) onScenarioTriggered('NEW_REPORT');
    } catch (err) {
      console.error(err);
      setLastAction(`Failed to file report: ${err.message}`);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-6 rounded-2xl bg-[#0E131F] border border-slate-800 shadow-2xl space-y-6">
      <div className="flex items-center justify-between border-b border-slate-800 pb-4">
        <div className="flex items-center gap-3">
          <div className="p-2.5 rounded-xl bg-amber-500/10 text-amber-400 border border-amber-500/20">
            <SlidersHorizontal className="w-5 h-5" />
          </div>
          <div>
            <h2 className="text-base font-bold text-white flex items-center gap-2">
              SIH Simulation & Disaster Injection Lab
              <span className="text-[10px] uppercase font-mono px-2 py-0.5 rounded bg-amber-500/20 text-amber-300 border border-amber-500/30">
                Evaluation Mode
              </span>
            </h2>
            <p className="text-xs text-slate-400">
              Instantly test system adaptability, hazard propagation, and automated rerouting without waiting for real incidents.
            </p>
          </div>
        </div>

        {onClose && (
          <button
            onClick={onClose}
            className="text-slate-400 hover:text-white text-xs font-semibold px-3 py-1.5 rounded-lg bg-slate-800"
          >
            Close
          </button>
        )}
      </div>

      {lastAction && (
        <div className="p-3 rounded-xl bg-emerald-500/10 border border-emerald-500/30 text-emerald-300 text-xs flex items-center gap-2 font-medium">
          <CheckCircle2 className="w-4 h-4 text-emerald-400" />
          <span>{lastAction}</span>
        </div>
      )}

      {/* Scenarios Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {scenarios.map((scen) => {
          const Icon = scen.icon;
          return (
            <div
              key={scen.id}
              className={`p-4 rounded-xl border bg-gradient-to-br ${scen.color} flex flex-col justify-between space-y-3 transition hover:scale-[1.01]`}
            >
              <div className="flex items-start gap-3">
                <div className="p-2 rounded-lg bg-slate-900/80 border border-slate-700">
                  <Icon className="w-5 h-5" />
                </div>
                <div>
                  <h3 className="text-xs font-bold text-slate-100">{scen.title}</h3>
                  <p className="text-[11px] text-slate-300 mt-1 leading-relaxed">{scen.desc}</p>
                </div>
              </div>

              <button
                disabled={loading}
                onClick={() => handleTrigger(scen.id)}
                className="w-full py-2 px-3 rounded-lg bg-slate-900/90 hover:bg-slate-800 text-white text-xs font-bold border border-slate-700 transition flex items-center justify-center gap-1.5 shadow-sm"
              >
                <Sparkles className="w-3.5 h-3.5 text-amber-400" />
                <span>Inject Scenario</span>
              </button>
            </div>
          );
        })}
      </div>

      {/* Extra Simulation Options */}
      <div className="pt-2 border-t border-slate-800/80 flex flex-wrap items-center justify-between gap-3">
        <div className="text-xs text-slate-400">
          Crowdsourced incident simulation:
        </div>
        <button
          disabled={loading}
          onClick={handleSimulateReport}
          className="py-2 px-4 rounded-xl bg-slate-800 hover:bg-slate-700 text-slate-200 text-xs font-semibold border border-slate-700 transition flex items-center gap-2"
        >
          <FileEdit className="w-4 h-4 text-emerald-400" />
          <span>Simulate New Field Road Report</span>
        </button>
      </div>
    </div>
  );
}
