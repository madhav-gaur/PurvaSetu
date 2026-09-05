import React, { useState, useEffect } from 'react';
import {
  Play,
  RotateCcw,
  ChevronRight,
  CheckCircle2,
  AlertTriangle,
  Truck,
  Navigation,
  CloudRain,
  ShieldCheck,
  X,
  Pause
} from 'lucide-react';
import { runEmergencyDemoStep } from '../services/api';

export default function EmergencyDemoModal({ isOpen, onClose, onDemoUpdate }) {
  const [currentStep, setCurrentStep] = useState(1);
  const [stepData, setStepData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [autoPlay, setAutoPlay] = useState(false);

  // Load step data from backend simulation engine
  const fetchStep = async (step) => {
    try {
      setLoading(true);
      const data = await runEmergencyDemoStep(step);
      setStepData(data);
      setCurrentStep(step);
      if (onDemoUpdate) onDemoUpdate(data);
    } catch (err) {
      console.error('Failed to run demo step', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (isOpen) {
      fetchStep(currentStep);
    }
  }, [isOpen]);

  // Auto-play timer
  useEffect(() => {
    let timer;
    if (autoPlay && isOpen) {
      timer = setTimeout(() => {
        if (currentStep < 14) {
          fetchStep(currentStep + 1);
        } else {
          setAutoPlay(false);
        }
      }, 3500);
    }
    return () => clearTimeout(timer);
  }, [autoPlay, currentStep, isOpen]);

  if (!isOpen) return null;

  const totalSteps = 14;
  const progressPercent = Math.round((currentStep / totalSteps) * 100);

  const getStageBadge = (stage) => {
    switch (stage) {
      case 'PLANNING':
        return 'bg-blue-500/20 text-blue-300 border-blue-500/30';
      case 'IN_TRANSIT':
        return 'bg-amber-500/20 text-amber-300 border-amber-500/30';
      case 'DISASTER_DETECTED':
        return 'bg-red-500/20 text-red-300 border-red-500/30 animate-pulse';
      case 'REROUTED':
        return 'bg-emerald-500/20 text-emerald-300 border-emerald-500/30';
      case 'DELIVERED':
        return 'bg-cyan-500/20 text-cyan-300 border-cyan-500/30';
      default:
        return 'bg-slate-500/20 text-slate-300 border-slate-500/30';
    }
  };

  return (
    <div className="fixed inset-0 z-[2000] flex items-center justify-center p-4 bg-black/80 backdrop-blur-md">
      <div className="demo-modal w-full max-w-4xl bg-[#0B0F19] border border-slate-700/80 rounded-3xl shadow-2xl overflow-hidden flex flex-col max-h-[90vh]">
        {/* Header */}
        <div className="px-6 py-4 border-b border-slate-800 bg-[#0E131F] flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-xl bg-gradient-to-tr from-red-600 via-rose-500 to-amber-500 p-0.5 flex items-center justify-center">
              <div className="w-full h-full bg-[#0E131F] rounded-[10px] flex items-center justify-center">
                <Truck className="w-5 h-5 text-red-400" />
              </div>
            </div>
            <div>
              <div className="flex items-center gap-2">
                <h2 className="text-base font-extrabold text-white tracking-tight">
                  SIH EMERGENCY LOGISTICS DEMO
                </h2>
                <span className={`text-[10px] font-mono px-2 py-0.5 rounded border font-semibold ${getStageBadge(stepData?.stage)}`}>
                  {stepData?.stage || 'SIMULATION'}
                </span>
              </div>
              <p className="text-xs text-slate-400">
                End-to-End Life-Saving Vaccine Delivery with Hazard Detection & Dynamic Rerouting
              </p>
            </div>
          </div>

          <button
            onClick={onClose}
            className="p-2 rounded-xl text-slate-400 hover:text-white hover:bg-slate-800 transition"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        {/* Progress Bar */}
        <div className="w-full bg-slate-900 h-2">
          <div
            className="h-full bg-gradient-to-r from-red-500 via-amber-500 to-emerald-500 transition-all duration-500"
            style={{ width: `${progressPercent}%` }}
          />
        </div>

        {/* Content Area */}
        <div className="p-6 overflow-y-auto space-y-6 flex-1">
          {/* Step Banner */}
          <div className="p-5 rounded-2xl bg-gradient-to-r from-slate-900 via-[#111827] to-slate-900 border border-slate-800 shadow-lg">
            <div className="flex items-center justify-between text-xs font-mono text-slate-400 mb-2">
              <span className="font-bold text-amber-400">Step {currentStep} of {totalSteps}</span>
              <span>{progressPercent}% Complete</span>
            </div>
            <h3 className="text-lg font-extrabold text-white mb-1.5">
              {stepData?.stepTitle || 'Initializing demonstration...'}
            </h3>
            <p className="text-xs text-slate-300 leading-relaxed">
              {stepData?.stepDescription || 'Preparing consignments and routing matrix.'}
            </p>
          </div>

          {/* Demonstration Corridor Comparison View */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            {/* Primary Corridor Card */}
            <div className={`p-4 rounded-2xl border transition-all ${currentStep >= 6 && currentStep <= 11
              ? 'bg-red-950/20 border-red-500/50 shadow-lg shadow-red-950/40'
              : 'bg-slate-900/60 border-slate-800'
              }`}>
              <div className="flex items-center justify-between mb-2">
                <span className="text-xs font-bold text-slate-200">
                  Primary Corridor (NH-6 via Sonapur)
                </span>
                <span className={`text-[10px] font-mono px-2 py-0.5 rounded font-bold ${currentStep >= 6 ? 'bg-red-500/20 text-red-300 border border-red-500/40' : 'bg-blue-500/20 text-blue-300'
                  }`}>
                  {currentStep >= 6 ? '84% RISK [HIGH HAZARD]' : '32% RISK [CLEAR]'}
                </span>
              </div>
              <div className="space-y-1.5 text-xs text-slate-300">
                <p><strong>Distance:</strong> 298 km (Standard mountain route)</p>
                <p><strong>Est. Transit Time:</strong> 6h 15m</p>
                <p><strong>Vulnerability:</strong> High landslide probability along steep slope cuts</p>
                {currentStep >= 6 && (
                  <p className="text-red-400 font-semibold pt-1 border-t border-red-900/50">
                    ⚠️ Active 40m debris mudslide & 88mm torrential rainfall blocking transit!
                  </p>
                )}
              </div>
            </div>

            {/* Alternative Safe Bypass Card */}
            <div className={`p-4 rounded-2xl border transition-all ${currentStep >= 9
              ? 'bg-emerald-950/20 border-emerald-500/50 shadow-lg shadow-emerald-950/40'
              : 'bg-slate-900/40 border-slate-800/80 opacity-70'
              }`}>
              <div className="flex items-center justify-between mb-2">
                <span className="text-xs font-bold text-emerald-300 flex items-center gap-1.5">
                  <ShieldCheck className="w-4 h-4 text-emerald-400" />
                  Northern Disaster Bypass (NH-27 via Lumding)
                </span>
                <span className="text-[10px] font-mono px-2 py-0.5 rounded font-bold bg-emerald-500/20 text-emerald-300 border border-emerald-500/40">
                  22% RISK [SAFE]
                </span>
              </div>
              <div className="space-y-1.5 text-xs text-slate-300">
                <p><strong>Distance:</strong> 342 km (+44 km bypass detour)</p>
                <p><strong>Est. Transit Time:</strong> 7h 05m (+50 mins transit)</p>
                <p><strong>Advantage:</strong> All-weather wide valley alignment avoiding unstable Sonapur hills</p>
                {currentStep >= 9 && (
                  <p className="text-emerald-400 font-semibold pt-1 border-t border-emerald-900/50">
                    ✅ AI Engine selects safe bypass: 78% lower hazard risk for critical vaccines!
                  </p>
                )}
              </div>
            </div>
          </div>

          {/* Live System Execution Logs */}
          <div className="p-4 rounded-2xl bg-black/60 border border-slate-800 font-mono text-xs space-y-1.5">
            <div className="text-[11px] font-bold text-slate-400 uppercase tracking-wider mb-2 flex items-center justify-between">
              <span>Platform Intelligence Event Stream</span>
              <span className="text-[10px] text-emerald-400 animate-pulse">● LIVE</span>
            </div>
            {stepData?.logs && stepData.logs.map((log, index) => (
              <div key={index} className="flex items-start gap-2 text-slate-300">
                <ChevronRight className="w-3.5 h-3.5 text-emerald-400 shrink-0 mt-0.5" />
                <span>{log}</span>
              </div>
            ))}
          </div>
        </div>

        {/* Footer Navigation & Controls */}
        <div className="px-6 py-4 border-t border-slate-800 bg-[#0E131F] flex items-center justify-between gap-4">
          <div className="flex items-center gap-2">
            <button
              onClick={() => fetchStep(1)}
              className="px-3 py-2 rounded-xl bg-slate-800 hover:bg-slate-700 text-slate-300 text-xs font-semibold flex items-center gap-1.5 transition"
            >
              <RotateCcw className="w-3.5 h-3.5" />
              <span>Reset</span>
            </button>

            <button
              onClick={() => setAutoPlay(!autoPlay)}
              className={`px-3 py-2 rounded-xl text-xs font-semibold flex items-center gap-1.5 transition border ${autoPlay
                ? 'bg-amber-500/20 text-amber-300 border-amber-500/30'
                : 'bg-slate-800 hover:bg-slate-700 text-slate-300 border-slate-700'
                }`}
            >
              {autoPlay ? <Pause className="w-3.5 h-3.5" /> : <Play className="w-3.5 h-3.5" />}
              <span>{autoPlay ? 'Pause Auto-Run' : 'Auto Play Demo'}</span>
            </button>
          </div>

          <div className="flex items-center gap-3">
            <button
              disabled={currentStep <= 1 || loading}
              onClick={() => fetchStep(currentStep - 1)}
              className="px-4 py-2 rounded-xl bg-slate-800 hover:bg-slate-700 disabled:opacity-40 text-slate-200 text-xs font-semibold transition"
            >
              Previous
            </button>

            <button
              disabled={currentStep >= totalSteps || loading}
              onClick={() => fetchStep(currentStep + 1)}
              className="px-5 py-2 rounded-xl bg-gradient-to-r from-emerald-600 to-teal-600 hover:from-emerald-500 hover:to-teal-500 disabled:opacity-40 text-white text-xs font-bold transition flex items-center gap-1.5 shadow-lg shadow-emerald-950/40"
            >
              <span>Next Demo Step</span>
              <ChevronRight className="w-4 h-4" />
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
