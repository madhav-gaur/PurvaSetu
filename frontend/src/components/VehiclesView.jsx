import React, { useState } from 'react';
import { 
  Truck, 
  MapPin, 
  Phone, 
  Gauge, 
  Navigation, 
  CheckCircle2, 
  History, 
  Play, 
  RefreshCw 
} from 'lucide-react';
import { updateVehicleLocation, getVehicleLocations } from '../services/api';

export default function VehiclesView({ vehicles = [], onVehicleUpdated }) {
  const [selectedVehicle, setSelectedVehicle] = useState(vehicles[0] || null);
  const [breadcrumbs, setBreadcrumbs] = useState([]);
  const [loadingHistory, setLoadingHistory] = useState(false);
  const [simulating, setSimulating] = useState(false);

  const handleSelect = async (veh) => {
    setSelectedVehicle(veh);
    try {
      setLoadingHistory(true);
      const history = await getVehicleLocations(veh.id);
      setBreadcrumbs(history);
    } catch (err) {
      console.error(err);
    } finally {
      setLoadingHistory(false);
    }
  };

  const handleSimulateGPSMove = async () => {
    if (!selectedVehicle) return;
    try {
      setSimulating(true);
      // Nudge coordinates slightly southward towards Silchar
      const newLat = (selectedVehicle.currentLatitude || 26.1445) - 0.08;
      const newLng = (selectedVehicle.currentLongitude || 91.7362) + 0.05;
      const newSpeed = 52.0;

      const updated = await updateVehicleLocation(selectedVehicle.id, {
        latitude: Math.round(newLat * 10000) / 10000,
        longitude: Math.round(newLng * 10000) / 10000,
        speed: newSpeed,
        timestamp: new Date().toISOString()
      });

      setSelectedVehicle(updated);
      const history = await getVehicleLocations(selectedVehicle.id);
      setBreadcrumbs(history);
      if (onVehicleUpdated) onVehicleUpdated();
    } catch (err) {
      console.error(err);
    } finally {
      setSimulating(false);
    }
  };

  return (
    <div className="p-6 space-y-6 max-w-[1700px] mx-auto">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 border-b border-slate-800 pb-4">
        <div>
          <h2 className="text-base font-bold text-white flex items-center gap-2">
            Fleet Operations & GPS Telemetry Tracking
          </h2>
          <p className="text-xs text-slate-400">
            Real-time automated vehicle positioning across North East mountain corridors.
          </p>
        </div>

        {selectedVehicle && (
          <button
            disabled={simulating}
            onClick={handleSimulateGPSMove}
            className="px-4 py-2 rounded-xl bg-emerald-600 hover:bg-emerald-500 disabled:opacity-50 text-white font-bold text-xs flex items-center gap-2 shadow-lg shadow-emerald-950/40 transition"
          >
            <Play className="w-3.5 h-3.5" />
            <span>{simulating ? 'Simulating Movement...' : `Advance Vehicle ${selectedVehicle.vehicleNumber} (GPS Ping)`}</span>
          </button>
        )}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Vehicles Table / Cards (2 Cols) */}
        <div className="lg:col-span-2 space-y-3">
          {vehicles.map((v) => {
            const isSelected = selectedVehicle?.id === v.id;
            return (
              <div
                key={v.id}
                onClick={() => handleSelect(v)}
                className={`p-4 rounded-2xl border cursor-pointer transition-all duration-200 ${
                  isSelected 
                    ? 'bg-slate-900 border-emerald-500/50 shadow-md shadow-emerald-950/30' 
                    : 'bg-[#0E131F]/90 border-slate-800 hover:border-slate-700'
                }`}
              >
                <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3 mb-3">
                  <div className="flex items-center gap-3">
                    <div className="p-2.5 rounded-xl bg-slate-800 border border-slate-700">
                      <Truck className={`w-5 h-5 ${v.status === 'IN_TRANSIT' ? 'text-emerald-400' : 'text-slate-400'}`} />
                    </div>
                    <div>
                      <div className="flex items-center gap-2">
                        <span className="text-sm font-bold text-white">{v.vehicleNumber}</span>
                        <span className={`text-[10px] font-mono px-2 py-0.5 rounded font-semibold ${
                          v.status === 'IN_TRANSIT' ? 'bg-emerald-500/20 text-emerald-300' : 'bg-slate-800 text-slate-300'
                        }`}>
                          {v.status}
                        </span>
                      </div>
                      <p className="text-xs text-slate-400">{v.vehicleType}</p>
                    </div>
                  </div>

                  <div className="flex items-center gap-4 text-xs font-mono">
                    <div className="text-right">
                      <span className="text-slate-500 text-[10px] block">Speed</span>
                      <span className="font-bold text-slate-200">{v.speed} km/h</span>
                    </div>
                    <div className="text-right">
                      <span className="text-slate-500 text-[10px] block">Coordinates</span>
                      <span className="text-slate-300">
                        {v.currentLatitude?.toFixed(4)}, {v.currentLongitude?.toFixed(4)}
                      </span>
                    </div>
                  </div>
                </div>

                <div className="flex flex-wrap items-center justify-between gap-2 pt-2.5 border-t border-slate-800/80 text-xs text-slate-400">
                  <div className="flex items-center gap-1.5">
                    <span>Driver: <strong className="text-slate-200">{v.driver}</strong></span>
                    <span>({v.driverPhone})</span>
                  </div>

                  {v.currentShipment && (
                    <div className="text-amber-300 font-medium">
                      Assigned: {v.currentShipment}
                    </div>
                  )}
                </div>
              </div>
            );
          })}
        </div>

        {/* Selected Vehicle Telemetry Breadcrumbs Panel (1 Col) */}
        <div className="p-5 rounded-2xl bg-[#0E131F]/90 border border-slate-800 shadow-xl space-y-4">
          <div className="flex items-center justify-between border-b border-slate-800 pb-3">
            <div className="flex items-center gap-2">
              <div className="p-2 rounded-xl bg-indigo-500/10 text-indigo-400">
                <History className="w-4 h-4" />
              </div>
              <div>
                <h3 className="text-xs font-bold uppercase tracking-wider text-slate-400">
                  GPS Breadcrumb Telemetry
                </h3>
                <p className="text-sm font-bold text-white">
                  {selectedVehicle?.vehicleNumber || 'Select a vehicle'}
                </p>
              </div>
            </div>
          </div>

          {loadingHistory ? (
            <div className="p-6 text-center text-xs text-slate-400 animate-pulse">
              Retrieving breadcrumb trail from database...
            </div>
          ) : breadcrumbs.length === 0 ? (
            <div className="p-6 text-center text-xs text-slate-500">
              No historical GPS points recorded yet.
            </div>
          ) : (
            <div className="space-y-2.5 overflow-y-auto max-h-[450px] pr-1">
              {breadcrumbs.map((b, idx) => (
                <div key={b.id || idx} className="p-3 rounded-xl bg-slate-900/60 border border-slate-800 text-xs space-y-1">
                  <div className="flex items-center justify-between text-slate-300">
                    <span className="font-mono text-emerald-400">
                      Lat {b.latitude?.toFixed(4)}, Lon {b.longitude?.toFixed(4)}
                    </span>
                    <span className="text-[10px] text-slate-500 font-mono">
                      {b.speed} km/h
                    </span>
                  </div>
                  <div className="text-[10px] text-slate-500 font-mono">
                    {new Date(b.timestamp).toLocaleString()}
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
