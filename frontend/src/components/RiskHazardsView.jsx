import React, { useState } from 'react';
import { 
  AlertTriangle, 
  Cpu, 
  Sparkles, 
  CheckCircle2, 
  ShieldAlert, 
  Flame, 
  Waves, 
  Mountain, 
  CloudRain 
} from 'lucide-react';
import { predictRisk } from '../services/api';

export default function RiskHazardsView({ hazards = [] }) {
  const [loading, setLoading] = useState(false);
  
  // Interactive Risk Parameters
  const [params, setParams] = useState({
    rainfall3Hour: 28.5,
    rainfall1Day: 88.0,
    humidity: 85,
    windSpeed: 24,
    roadCondition: 0.7,
    recentReports: 4,
    floodHistory: 0.6,
    landslideHistory: 0.85,
    securityRisk: 0.1,
    shipmentPriority: 'CRITICAL'
  });

  const [prediction, setPrediction] = useState({
    floodRisk: 0.78,
    landslideRisk: 0.84,
    roadDisruptionRisk: 0.72,
    weatherRisk: 0.68,
    overallRisk: 0.81,
    riskLevel: 'CRITICAL',
    predictionSource: 'FASTAPI_RANDOM_FOREST_ML',
    contributingFactors: [
      'Elevated flood inundation probability (78%)',
      'Steep slope rain saturation triggering landslide risk (84%)',
      'Active road blockage or damaged infrastructure (72%)'
    ],
    recommendation: 'High hazard risk detected. Reroute via safe disaster bypass recommended.'
  });

  const handlePredict = async () => {
    try {
      setLoading(true);
      const res = await predictRisk(params);
      setPrediction(res);
    } catch (err) {
      console.error('Prediction failed', err);
    } finally {
      setLoading(false);
    }
  };

  const getRiskColor = (score) => {
    if (score >= 0.81) return 'text-red-400';
    if (score >= 0.61) return 'text-amber-400';
    if (score >= 0.31) return 'text-yellow-400';
    return 'text-emerald-400';
  };

  const getRiskBg = (score) => {
    if (score >= 0.81) return 'bg-red-500/20 text-red-300 border-red-500/40';
    if (score >= 0.61) return 'bg-amber-500/20 text-amber-300 border-amber-500/40';
    if (score >= 0.31) return 'bg-yellow-500/20 text-yellow-300 border-yellow-500/40';
    return 'bg-emerald-500/20 text-emerald-300 border-emerald-500/40';
  };

  return (
    <div className="p-6 space-y-6 max-w-[1700px] mx-auto">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 border-b border-slate-800 pb-4">
        <div>
          <h2 className="text-base font-bold text-white flex items-center gap-2">
            Multi-Hazard Intelligence & AI Predictive Risk Engine
          </h2>
          <p className="text-xs text-slate-400">
            Real-time inference combining meteorology, geomorphological slope stability, and crowdsourced telemetry.
          </p>
        </div>

        {/* Engine Source Badge */}
        <div className="flex items-center gap-2 px-3 py-1.5 rounded-xl bg-slate-800/80 border border-slate-700 text-xs">
          <Cpu className="w-4 h-4 text-emerald-400" />
          <span className="text-slate-400">Prediction Engine:</span>
          <span className="font-bold text-emerald-300">
            {prediction.predictionSource?.includes('FASTAPI') 
              ? 'AI MODEL (Random Forest ML)' 
              : 'ANALYTICAL FALLBACK (Weighted Rule Engine)'}
          </span>
        </div>
      </div>

      {/* Simulator Playground & Output Cards */}
      <div className="grid grid-cols-1 lg:grid-cols-12 gap-6">
        {/* Sliders Input Panel (7 Cols) */}
        <div className="lg:col-span-7 p-6 rounded-2xl bg-[#0E131F] border border-slate-800 shadow-xl space-y-5">
          <div className="flex items-center justify-between border-b border-slate-800 pb-3">
            <h3 className="text-xs font-bold uppercase tracking-wider text-slate-300">
              Corridor Telemetry & Terrain Stress Parameters
            </h3>
            <button
              disabled={loading}
              onClick={handlePredict}
              className="px-4 py-1.5 rounded-xl bg-emerald-600 hover:bg-emerald-500 text-white font-bold text-xs flex items-center gap-1.5 shadow-md shadow-emerald-950/40 transition"
            >
              <Sparkles className="w-3.5 h-3.5" />
              <span>{loading ? 'Evaluating Model...' : 'Run Risk Inference'}</span>
            </button>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-5 text-xs">
            {/* 3h Rain */}
            <div>
              <div className="flex justify-between text-slate-300 mb-1.5">
                <span>3-Hour Rainfall Accumulation</span>
                <span className="font-mono text-cyan-400 font-bold">{params.rainfall3Hour} mm</span>
              </div>
              <input
                type="range"
                min="0"
                max="60"
                step="1"
                value={params.rainfall3Hour}
                onChange={(e) => setParams({ ...params, rainfall3Hour: parseFloat(e.target.value) })}
                className="w-full accent-cyan-500"
              />
            </div>

            {/* 24h Rain */}
            <div>
              <div className="flex justify-between text-slate-300 mb-1.5">
                <span>24-Hour Rainfall Accumulation</span>
                <span className="font-mono text-cyan-400 font-bold">{params.rainfall1Day} mm</span>
              </div>
              <input
                type="range"
                min="0"
                max="250"
                step="5"
                value={params.rainfall1Day}
                onChange={(e) => setParams({ ...params, rainfall1Day: parseFloat(e.target.value) })}
                className="w-full accent-cyan-500"
              />
            </div>

            {/* Mountain Slope Landslide History */}
            <div>
              <div className="flex justify-between text-slate-300 mb-1.5">
                <span>Mountain Slope Landslide Index</span>
                <span className="font-mono text-rose-400 font-bold">{Math.round(params.landslideHistory * 100)}%</span>
              </div>
              <input
                type="range"
                min="0"
                max="1"
                step="0.05"
                value={params.landslideHistory}
                onChange={(e) => setParams({ ...params, landslideHistory: parseFloat(e.target.value) })}
                className="w-full accent-rose-500"
              />
            </div>

            {/* River Plain Flood History */}
            <div>
              <div className="flex justify-between text-slate-300 mb-1.5">
                <span>River Plain Flood Vulnerability</span>
                <span className="font-mono text-blue-400 font-bold">{Math.round(params.floodHistory * 100)}%</span>
              </div>
              <input
                type="range"
                min="0"
                max="1"
                step="0.05"
                value={params.floodHistory}
                onChange={(e) => setParams({ ...params, floodHistory: parseFloat(e.target.value) })}
                className="w-full accent-blue-500"
              />
            </div>

            {/* Road Surface Damage */}
            <div>
              <div className="flex justify-between text-slate-300 mb-1.5">
                <span>Road Surface Degradation</span>
                <span className="font-mono text-amber-400 font-bold">{Math.round(params.roadCondition * 100)}%</span>
              </div>
              <input
                type="range"
                min="0"
                max="1"
                step="0.05"
                value={params.roadCondition}
                onChange={(e) => setParams({ ...params, roadCondition: parseFloat(e.target.value) })}
                className="w-full accent-amber-500"
              />
            </div>

            {/* Recent Incident Reports */}
            <div>
              <div className="flex justify-between text-slate-300 mb-1.5">
                <span>Active Field Damage Reports</span>
                <span className="font-mono text-orange-400 font-bold">{params.recentReports} Reports</span>
              </div>
              <input
                type="range"
                min="0"
                max="10"
                step="1"
                value={params.recentReports}
                onChange={(e) => setParams({ ...params, recentReports: parseInt(e.target.value) })}
                className="w-full accent-orange-500"
              />
            </div>
          </div>

          <div className="p-3 rounded-xl bg-slate-900/60 border border-slate-800 text-[11px] text-slate-400 leading-relaxed">
            * Note: Synthetic dataset trained specifically on North East topography (Assam, Meghalaya, Arunachal Pradesh, Sikkim). Provided for prototype decision support without asserting scientific certainty.
          </div>
        </div>

        {/* Prediction Results Display (5 Cols) */}
        <div className="lg:col-span-5 p-6 rounded-2xl bg-[#0E131F] border border-slate-800 shadow-xl space-y-5 flex flex-col justify-between">
          <div className="space-y-4">
            <div className="flex items-center justify-between border-b border-slate-800 pb-3">
              <span className="text-xs font-bold uppercase tracking-wider text-slate-400">
                Composite Risk Evaluation
              </span>
              <span className={`text-xs font-mono font-bold px-2.5 py-1 rounded-lg border ${getRiskBg(prediction.overallRisk)}`}>
                {prediction.riskLevel} RISK ({Math.round(prediction.overallRisk * 100)}%)
              </span>
            </div>

            {/* Risk Category Bars */}
            <div className="space-y-3 text-xs">
              <div>
                <div className="flex justify-between mb-1">
                  <span className="text-slate-300 flex items-center gap-1.5">
                    <Mountain className="w-3.5 h-3.5 text-rose-400" />
                    Landslide Saturation Risk
                  </span>
                  <span className={`font-bold font-mono ${getRiskColor(prediction.landslideRisk)}`}>
                    {Math.round(prediction.landslideRisk * 100)}%
                  </span>
                </div>
                <div className="w-full h-2 bg-slate-800 rounded-full overflow-hidden">
                  <div className="h-full bg-rose-500" style={{ width: `${Math.round(prediction.landslideRisk * 100)}%` }} />
                </div>
              </div>

              <div>
                <div className="flex justify-between mb-1">
                  <span className="text-slate-300 flex items-center gap-1.5">
                    <Waves className="w-3.5 h-3.5 text-sky-400" />
                    River Plain Flood Inundation
                  </span>
                  <span className={`font-bold font-mono ${getRiskColor(prediction.floodRisk)}`}>
                    {Math.round(prediction.floodRisk * 100)}%
                  </span>
                </div>
                <div className="w-full h-2 bg-slate-800 rounded-full overflow-hidden">
                  <div className="h-full bg-sky-500" style={{ width: `${Math.round(prediction.floodRisk * 100)}%` }} />
                </div>
              </div>

              <div>
                <div className="flex justify-between mb-1">
                  <span className="text-slate-300 flex items-center gap-1.5">
                    <AlertTriangle className="w-3.5 h-3.5 text-amber-400" />
                    Road Disruption & Blockage
                  </span>
                  <span className={`font-bold font-mono ${getRiskColor(prediction.roadDisruptionRisk)}`}>
                    {Math.round(prediction.roadDisruptionRisk * 100)}%
                  </span>
                </div>
                <div className="w-full h-2 bg-slate-800 rounded-full overflow-hidden">
                  <div className="h-full bg-amber-500" style={{ width: `${Math.round(prediction.roadDisruptionRisk * 100)}%` }} />
                </div>
              </div>
            </div>

            {/* Contributing Factors */}
            <div className="p-3.5 rounded-xl bg-slate-900/70 border border-slate-800 space-y-2">
              <span className="text-[11px] font-bold uppercase tracking-wider text-slate-400">
                Key Hazard Drivers
              </span>
              <div className="space-y-1 text-xs text-slate-300">
                {(prediction.contributingFactors || []).map((factor, idx) => (
                  <div key={idx} className="flex items-center gap-2">
                    <span className="w-1.5 h-1.5 rounded-full bg-amber-400"></span>
                    <span>{factor}</span>
                  </div>
                ))}
              </div>
            </div>
          </div>

          <div className="p-3 rounded-xl bg-slate-900 border border-slate-800 text-xs">
            <span className="text-slate-400 block mb-1">AI Recommendation:</span>
            <p className="font-semibold text-emerald-400">{prediction.recommendation}</p>
          </div>
        </div>
      </div>
    </div>
  );
}
