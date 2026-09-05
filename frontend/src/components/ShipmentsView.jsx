import React, { useState } from 'react';
import { createPortal } from 'react-dom';
import {
  Package,
  Plus,
  Truck,
  Calendar,
  Weight,
  AlertCircle,
  CheckCircle2,
  X
} from 'lucide-react';
import { createShipment, assignVehicle } from '../services/api';

export default function ShipmentsView({ shipments = [], vehicles = [], onShipmentsUpdated }) {
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [selectedShipmentForAssign, setSelectedShipmentForAssign] = useState(null);
  const [selectedVehicleId, setSelectedVehicleId] = useState('');
  const [loading, setLoading] = useState(false);
  const [notice, setNotice] = useState(null);

  // New Shipment Form
  const [formData, setFormData] = useState({
    trackingNumber: `NER-MED-${Math.floor(100 + Math.random() * 900)}`,
    source: 'Guwahati Logistics Hub, Assam',
    destination: 'Silchar Medical Hub, Assam',
    priority: 'CRITICAL',
    cargoType: 'Cold-Chain Insulin & Emergency Anti-Venom',
    weight: 2.5
  });

  const handleCreate = async (e) => {
    e.preventDefault();
    try {
      setLoading(true);
      await createShipment(formData);
      setShowCreateModal(false);
      setNotice('New shipment created successfully!');
      if (onShipmentsUpdated) onShipmentsUpdated();
    } catch (err) {
      console.error(err);
      setNotice(`Failed to create shipment: ${err.message}`);
    } finally {
      setLoading(false);
    }
  };

  const handleAssign = async () => {
    if (!selectedShipmentForAssign || !selectedVehicleId) return;
    try {
      setLoading(true);
      await assignVehicle(selectedShipmentForAssign.id, Number(selectedVehicleId));
      setSelectedShipmentForAssign(null);
      setNotice('Vehicle assigned successfully!');
      if (onShipmentsUpdated) onShipmentsUpdated();
    } catch (err) {
      console.error(err);
      setNotice(`Failed to assign vehicle: ${err.message}`);
    } finally {
      setLoading(false);
    }
  };

  const getPriorityBadge = (priority) => {
    switch (priority) {
      case 'CRITICAL':
        return 'bg-red-500/20 text-red-300 border-red-500/40 animate-pulse';
      case 'HIGH':
        return 'bg-amber-500/20 text-amber-300 border-amber-500/40';
      case 'MEDIUM':
        return 'bg-blue-500/20 text-blue-300 border-blue-500/40';
      default:
        return 'bg-slate-700 text-slate-300 border-slate-600';
    }
  };

  return (
    <div className="p-6 space-y-6 max-w-[1700px] mx-auto">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 border-b border-slate-800 pb-4">
        <div>
          <h2 className="text-base font-bold text-white flex items-center gap-2">
            Consignment & Relief Logistics Management
          </h2>
          <p className="text-xs text-slate-400">
            Prioritizing critical life-saving consignments through AI-monitored disaster corridors.
          </p>
        </div>

        <button
          onClick={() => setShowCreateModal(true)}
          className="px-4 py-2 rounded-xl bg-emerald-600 hover:bg-emerald-500 text-white font-bold text-xs flex items-center gap-2 shadow-lg shadow-emerald-950/40 transition"
        >
          <Plus className="w-4 h-4" />
          <span>New Consignment</span>
        </button>
      </div>

      {notice && (
        <div className="p-3.5 rounded-xl bg-emerald-500/10 border border-emerald-500/30 text-emerald-300 text-xs flex items-center gap-2 font-medium">
          <CheckCircle2 className="w-4 h-4 text-emerald-400 shrink-0" />
          <span>{notice}</span>
        </div>
      )}

      {/* Shipments Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {shipments.map((s) => (
          <div
            key={s.id}
            className={`p-5 rounded-2xl border transition-all duration-200 bg-[#0E131F]/90 ${s.priority === 'CRITICAL' ? 'border-red-500/40 shadow-lg shadow-red-950/20' : 'border-slate-800'
              }`}
          >
            <div className="flex items-start justify-between gap-3 mb-3">
              <div>
                <div className="flex items-center gap-2">
                  <span className="text-sm font-bold text-white font-mono">{s.trackingNumber}</span>
                  <span className={`text-[10px] font-mono px-2 py-0.5 rounded border font-bold ${getPriorityBadge(s.priority)}`}>
                    {s.priority}
                  </span>
                </div>
                <p className="text-xs font-semibold text-slate-200 mt-1">{s.cargoType}</p>
              </div>

              <span className="text-[11px] font-medium text-emerald-400 px-2 py-0.5 rounded bg-emerald-500/10 border border-emerald-500/20">
                {s.status}
              </span>
            </div>

            <div className="grid grid-cols-2 gap-2 text-xs text-slate-300 py-3 border-y border-slate-800/80 mb-3">
              <div>
                <span className="text-[10px] text-slate-500 block">Origin</span>
                <span className="font-medium">{s.source}</span>
              </div>
              <div>
                <span className="text-[10px] text-slate-500 block">Destination</span>
                <span className="font-medium">{s.destination}</span>
              </div>
            </div>

            <div className="flex items-center justify-between text-xs">
              <div className="flex items-center gap-4 text-slate-400">
                <span className="flex items-center gap-1">
                  <Weight className="w-3.5 h-3.5" />
                  {s.weight} Tons
                </span>
                {s.vehicle ? (
                  <span className="flex items-center gap-1 text-emerald-400 font-medium">
                    <Truck className="w-3.5 h-3.5" />
                    {s.vehicle.vehicleNumber}
                  </span>
                ) : (
                  <span className="text-amber-400 text-[11px]">No vehicle assigned</span>
                )}
              </div>

              {!s.vehicle && (
                <button
                  onClick={() => setSelectedShipmentForAssign(s)}
                  className="px-3 py-1 rounded-lg bg-slate-800 hover:bg-slate-700 text-slate-200 text-[11px] font-semibold transition"
                >
                  Assign Vehicle
                </button>
              )}
            </div>
          </div>
        ))}
      </div>

      {/* Create Shipment Modal */}
      {showCreateModal && (
        createPortal(<div className="fixed inset-0 z-[2000] flex items-center justify-center p-4 bg-black/80 backdrop-blur-sm">
          <div className="w-full max-w-lg bg-[#0E131F] border border-slate-700 rounded-3xl p-6 shadow-2xl space-y-4">
            <div className="flex items-center justify-between border-b border-slate-800 pb-3">
              <h3 className="text-sm font-bold text-white">Create New Consignment</h3>
              <button onClick={() => setShowCreateModal(false)} className="text-slate-400 hover:text-white">
                <X className="w-5 h-5" />
              </button>
            </div>

            <form onSubmit={handleCreate} className="space-y-3 text-xs">
              <div>
                <label className="block text-slate-400 mb-1">Consignment ID / Tracking Number</label>
                <input
                  type="text"
                  value={formData.trackingNumber}
                  onChange={(e) => setFormData({ ...formData, trackingNumber: e.target.value })}
                  className="w-full bg-slate-900 border border-slate-700 rounded-xl px-3 py-2 text-white font-mono"
                  required
                />
              </div>

              <div>
                <label className="block text-slate-400 mb-1">Cargo Description</label>
                <input
                  type="text"
                  value={formData.cargoType}
                  onChange={(e) => setFormData({ ...formData, cargoType: e.target.value })}
                  className="w-full bg-slate-900 border border-slate-700 rounded-xl px-3 py-2 text-white"
                  required
                />
              </div>

              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-slate-400 mb-1">Origin Terminal</label>
                  <input
                    type="text"
                    value={formData.source}
                    onChange={(e) => setFormData({ ...formData, source: e.target.value })}
                    className="w-full bg-slate-900 border border-slate-700 rounded-xl px-3 py-2 text-white"
                    required
                  />
                </div>
                <div>
                  <label className="block text-slate-400 mb-1">Destination Terminal</label>
                  <input
                    type="text"
                    value={formData.destination}
                    onChange={(e) => setFormData({ ...formData, destination: e.target.value })}
                    className="w-full bg-slate-900 border border-slate-700 rounded-xl px-3 py-2 text-white"
                    required
                  />
                </div>
              </div>

              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-slate-400 mb-1">Priority Classification</label>
                  <select
                    value={formData.priority}
                    onChange={(e) => setFormData({ ...formData, priority: e.target.value })}
                    className="w-full bg-slate-900 border border-slate-700 rounded-xl px-3 py-2 text-white font-mono"
                  >
                    <option value="CRITICAL">CRITICAL (Emergency Medicine)</option>
                    <option value="HIGH">HIGH (Relief Supplies)</option>
                    <option value="MEDIUM">MEDIUM (Commercial)</option>
                    <option value="LOW">LOW (Standard Cargo)</option>
                  </select>
                </div>
                <div>
                  <label className="block text-slate-400 mb-1">Cargo Weight (Tons)</label>
                  <input
                    type="number"
                    step="0.1"
                    value={formData.weight}
                    onChange={(e) => setFormData({ ...formData, weight: parseFloat(e.target.value) })}
                    className="w-full bg-slate-900 border border-slate-700 rounded-xl px-3 py-2 text-white"
                    required
                  />
                </div>
              </div>

              <button
                type="submit"
                disabled={loading}
                className="w-full py-2.5 mt-2 rounded-xl bg-emerald-600 hover:bg-emerald-500 font-bold text-white transition"
              >
                {loading ? 'Creating...' : 'Register Consignment'}
              </button>
            </form>
          </div>
        </div>, document.body)
      )}

      {/* Assign Vehicle Modal */}
      {selectedShipmentForAssign && (
        createPortal(<div className="fixed inset-0 z-[2000] flex items-center justify-center p-4 bg-black/80 backdrop-blur-sm">
          <div className="w-full max-w-md bg-[#0E131F] border border-slate-700 rounded-3xl p-6 shadow-2xl space-y-4">
            <div className="flex items-center justify-between border-b border-slate-800 pb-3">
              <h3 className="text-sm font-bold text-white">
                Assign Fleet Vehicle to {selectedShipmentForAssign.trackingNumber}
              </h3>
              <button onClick={() => setSelectedShipmentForAssign(null)} className="text-slate-400 hover:text-white">
                <X className="w-5 h-5" />
              </button>
            </div>

            <div className="space-y-3 text-xs">
              <label className="block text-slate-400">Select Available Fleet Vehicle</label>
              <select
                value={selectedVehicleId}
                onChange={(e) => setSelectedVehicleId(e.target.value)}
                className="w-full bg-slate-900 border border-slate-700 rounded-xl px-3 py-2 text-white"
              >
                <option value="">-- Choose Vehicle --</option>
                {vehicles.map((v) => (
                  <option key={v.id} value={v.id}>
                    {v.vehicleNumber} - {v.vehicleType} ({v.driver})
                  </option>
                ))}
              </select>

              <button
                onClick={handleAssign}
                disabled={loading || !selectedVehicleId}
                className="w-full py-2.5 rounded-xl bg-emerald-600 hover:bg-emerald-500 disabled:opacity-50 font-bold text-white transition"
              >
                {loading ? 'Assigning...' : 'Confirm Vehicle Assignment'}
              </button>
            </div>
          </div>
        </div>, document.body)
      )}
    </div>
  );
}
