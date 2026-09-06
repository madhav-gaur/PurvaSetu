import React from 'react';
import {
  CloudRain,
  Wind,
  Droplets,
  Thermometer,
  AlertTriangle,
  CheckCircle2,
  Compass
} from 'lucide-react';
import CustomSelect from './CustomSelect';

export default function WeatherWidget({ weather, onSelectLocation, selectedLocationName }) {
  if (!weather) {
    return (
      <div className="p-4 rounded-2xl bg-[#111827]/80 border border-slate-800 animate-pulse text-xs text-slate-400">
        Loading real-time Open-Meteo weather telemetry...
      </div>
    );
  }

  const locations = [
    { name: 'Guwahati Logistics Hub', lat: 26.1445, lon: 91.7362 },
    { name: 'Silchar Medical Hub', lat: 24.8333, lon: 92.7789 },
    { name: 'Dibang Valley Relief Post', lat: 28.1400, lon: 95.8300 },
    { name: 'Anjaw Border Post', lat: 27.8860, lon: 96.7970 },
    { name: 'Darjeeling Foothills', lat: 27.0410, lon: 88.2663 },
    { name: 'Shillong Plateau', lat: 25.5788, lon: 91.8933 }
  ];

  return (
    <div className="p-5 rounded-2xl bg-[#0E131F]/90 border border-slate-800 shadow-xl space-y-4">
      {/* Header & Location Selector */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3 border-b border-slate-800/80 pb-3">
        <div className="flex items-center gap-2">
          <div className="p-2 rounded-xl bg-cyan-500/10 text-cyan-400 border border-cyan-500/20">
            <CloudRain className="w-5 h-5" />
          </div>
          <div>
            <h3 className="text-xs font-bold uppercase tracking-wider text-slate-400">
              Open-Meteo Atmospheric Telemetry
            </h3>
            <p className="text-sm font-bold text-white flex items-center gap-1.5">
              {weather.locationName || selectedLocationName || 'Guwahati Hub'}
              <span className="text-[10px] px-1.5 py-0.5 rounded bg-cyan-500/20 text-cyan-300 font-mono">
                LIVE RADAR
              </span>
            </p>
          </div>
        </div>

        {/* Location Dropdown */}
        <CustomSelect
          value={weather.locationName || selectedLocationName}
          onChange={(value) => {
            const loc = locations.find(l => l.name === value);
            if (loc && onSelectLocation) {
              onSelectLocation(loc.lat, loc.lon, loc.name);
            }
          }}
          options={locations.map(l => ({ value: l.name, label: l.name }))}
          ariaLabel="Weather location"
          className="w-full sm:w-64"
        />
      </div>

      {/* Main Temperature & Weather Condition Grid */}
      <div className="grid grid-cols-2 sm:grid-cols-4 gap-3">
        <div className="p-3 rounded-xl bg-slate-900/60 border border-slate-800/70">
          <div className="flex items-center gap-2 text-slate-400 text-xs mb-1">
            <Thermometer className="w-3.5 h-3.5 text-rose-400" />
            <span>Temperature</span>
          </div>
          <div className="text-xl font-extrabold text-white">
            {weather.temperature != null ? `${weather.temperature}°C` : '--'}
          </div>
          <div className="text-[10px] text-slate-400 mt-0.5 truncate">
            {weather.weatherCondition || 'Clear'}
          </div>
        </div>

        <div className="p-3 rounded-xl bg-slate-900/60 border border-slate-800/70">
          <div className="flex items-center gap-2 text-slate-400 text-xs mb-1">
            <Droplets className="w-3.5 h-3.5 text-cyan-400" />
            <span>Humidity</span>
          </div>
          <div className="text-xl font-extrabold text-cyan-300">
            {weather.humidity != null ? `${weather.humidity}%` : '--'}
          </div>
          <div className="text-[10px] text-slate-400 mt-0.5">
            Precip: {weather.precipitation != null ? `${weather.precipitation} mm` : '0 mm'}
          </div>
        </div>

        <div className="p-3 rounded-xl bg-slate-900/60 border border-slate-800/70">
          <div className="flex items-center gap-2 text-slate-400 text-xs mb-1">
            <Wind className="w-3.5 h-3.5 text-amber-400" />
            <span>Wind Speed</span>
          </div>
          <div className="text-xl font-extrabold text-amber-300">
            {weather.windSpeed != null ? `${weather.windSpeed} km/h` : '--'}
          </div>
          <div className="text-[10px] text-slate-400 mt-0.5">
            Gale force: {weather.windSpeed > 30 ? 'High' : 'Normal'}
          </div>
        </div>

        <div className="p-3 rounded-xl bg-slate-900/60 border border-slate-800/70">
          <div className="flex items-center gap-2 text-slate-400 text-xs mb-1">
            <CloudRain className="w-3.5 h-3.5 text-indigo-400" />
            <span>24h Rainfall</span>
          </div>
          <div className="text-xl font-extrabold text-indigo-300">
            {weather.rainfall1Day != null ? `${weather.rainfall1Day} mm` : '0 mm'}
          </div>
          <div className="text-[10px] text-slate-400 mt-0.5">
            3h Acc: {weather.rainfall3Hour != null ? `${weather.rainfall3Hour} mm` : '0 mm'}
          </div>
        </div>
      </div>

      {/* Geomorphological Risk Bars based on Weather */}
      <div className="p-3.5 rounded-xl bg-slate-900/40 border border-slate-800/80 space-y-2">
        <div className="flex items-center justify-between text-xs">
          <span className="text-slate-400 font-medium">Precipitation-Induced Flood Risk</span>
          <span className={`font-bold ${weather.floodRisk > 0.6 ? 'text-red-400' : 'text-emerald-400'}`}>
            {Math.round((weather.floodRisk || 0) * 100)}%
          </span>
        </div>
        <div className="w-full h-2 bg-slate-800 rounded-full overflow-hidden">
          <div
            className={`h-full transition-all duration-500 ${weather.floodRisk > 0.6 ? 'bg-gradient-to-r from-amber-500 to-red-500' : 'bg-emerald-500'
              }`}
            style={{ width: `${Math.min(100, (weather.floodRisk || 0) * 100)}%` }}
          />
        </div>

        <div className="flex items-center justify-between text-xs pt-1">
          <span className="text-slate-400 font-medium">Rainfall Saturation Landslide Index</span>
          <span className={`font-bold ${weather.landslideRisk > 0.6 ? 'text-red-400' : 'text-emerald-400'}`}>
            {Math.round((weather.landslideRisk || 0) * 100)}%
          </span>
        </div>
        <div className="w-full h-2 bg-slate-800 rounded-full overflow-hidden">
          <div
            className={`h-full transition-all duration-500 ${weather.landslideRisk > 0.6 ? 'bg-gradient-to-r from-amber-500 to-red-500' : 'bg-emerald-500'
              }`}
            style={{ width: `${Math.min(100, (weather.landslideRisk || 0) * 100)}%` }}
          />
        </div>
      </div>
    </div>
  );
}
