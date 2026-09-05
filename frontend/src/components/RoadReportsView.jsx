import React, { useState } from 'react';
import {
  FileWarning,
  Plus,
  CheckCircle2,
  Clock,
  MapPin,
  ShieldAlert,
  Image as ImageIcon,
  X
} from 'lucide-react';
import { createRoadReport, verifyRoadReport, resolveRoadReport } from '../services/api';

export default function RoadReportsView({ reports = [], onReportsUpdated }) {
  const [showModal, setShowModal] = useState(false);
  const [loading, setLoading] = useState(false);
  const [notice, setNotice] = useState(null);

  const [formData, setFormData] = useState({
    reporter: 'Officer Baruah (Field Patrol)',
    latitude: 25.1200,
    longitude: 92.3800,
    type: 'LANDSLIDE',
    severity: 'CRITICAL',
    description: 'Active mudslide and fallen rock debris near Sonapur Tunnel.',
    photoUrl: 'https://images.unsplash.com/photo-1547683905-f686c993aae5?auto=format&fit=crop&w=600&q=80'
  });

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      setLoading(true);
      await createRoadReport(formData);
      setShowModal(false);
      setNotice('Road report filed and registered into AI Hazard Engine!');
      if (onReportsUpdated) onReportsUpdated();
    } catch (err) {
      console.error(err);
      setNotice(`Failed to submit report: ${err.message}`);
    } finally {
      setLoading(false);
    }
  };

  const handleVerify = async (id) => {
    try {
      setLoading(true);
      await verifyRoadReport(id);
      setNotice(`Report #${id} verified by Operator! Hazard risk recalculated.`);
      if (onReportsUpdated) onReportsUpdated();
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleResolve = async (id) => {
    try {
      setLoading(true);
      await resolveRoadReport(id);
      setNotice(`Report #${id} marked as RESOLVED. Corridor restored.`);
      if (onReportsUpdated) onReportsUpdated();
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-6 space-y-6 max-w-[1700px] mx-auto">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 border-b border-slate-800 pb-4">
        <div>
          <h2 className="text-base font-bold text-white flex items-center gap-2">
            Crowdsourced Field Incidents & Road Damage Reports
          </h2>
          <p className="text-xs text-slate-400">
            Verified ground reports dynamically feed the AI risk score and route penalization.
          </p>
        </div>

        <button
          onClick={() => setShowModal(true)}
          className="px-4 py-2 rounded-xl bg-amber-600 hover:bg-amber-500 text-white font-bold text-xs flex items-center gap-2 shadow-lg shadow-amber-950/40 transition"
        >
          <Plus className="w-4 h-4" />
          <span>File Incident Report</span>
        </button>
      </div>

      {notice && (
        <div className="p-3.5 rounded-xl bg-emerald-500/10 border border-emerald-500/30 text-emerald-300 text-xs flex items-center gap-2 font-medium">
          <CheckCircle2 className="w-4 h-4 text-emerald-400 shrink-0" />
          <span>{notice}</span>
        </div>
      )}

      {/* Reports Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {reports.map((r) => (
          <div
            key={r.id}
            className="p-5 rounded-2xl bg-[#0E131F]/90 border border-slate-800 shadow-xl flex flex-col justify-between space-y-3"
          >
            <div>
              <div className="flex items-center justify-between gap-2 mb-2">
                <span className="text-xs font-bold text-white flex items-center gap-1.5">
                  <span className="w-2 h-2 rounded-full bg-amber-400"></span>
                  {r.type}
                </span>
                <div className="flex items-center gap-1.5">
                  <span className={`text-[10px] font-mono px-2 py-0.5 rounded font-bold ${r.severity === 'CRITICAL' ? 'bg-red-500/20 text-red-300 border border-red-500/40' : 'bg-amber-500/20 text-amber-300'
                    }`}>
                    {r.severity}
                  </span>
                  <span className={`text-[10px] font-mono px-2 py-0.5 rounded font-semibold ${r.status === 'VERIFIED' ? 'bg-emerald-500/20 text-emerald-300' : 'bg-slate-800 text-slate-400'
                    }`}>
                    {r.status}
                  </span>
                </div>
              </div>

              <p className="text-xs text-slate-300 leading-relaxed">{r.description}</p>

              {r.photoUrl && (
                <div className="mt-3 rounded-xl overflow-hidden border border-slate-800">
                  <img src={r.photoUrl} alt="Damage scene" className="w-full h-32 object-cover" />
                </div>
              )}
            </div>

            <div className="pt-3 border-t border-slate-800/80 text-[11px] text-slate-400 space-y-2">
              <div className="flex justify-between items-center">
                <span>By: <strong className="text-slate-200">{r.reporter}</strong></span>
                <span className="font-mono text-slate-500">
                  Lat {r.latitude?.toFixed(4)}, Lon {r.longitude?.toFixed(4)}
                </span>
              </div>

              {/* Operator Verification & Resolution Controls */}
              <div className="flex items-center gap-2 pt-1">
                {r.status === 'PENDING' && (
                  <button
                    onClick={() => handleVerify(r.id)}
                    className="flex-1 py-1.5 rounded-lg bg-emerald-600/20 hover:bg-emerald-600/30 text-emerald-300 border border-emerald-500/30 text-[11px] font-semibold transition"
                  >
                    Verify Report
                  </button>
                )}
                {r.status !== 'RESOLVED' && (
                  <button
                    onClick={() => handleResolve(r.id)}
                    className="flex-1 py-1.5 rounded-lg bg-slate-800 hover:bg-slate-700 text-slate-300 border border-slate-700 text-[11px] font-semibold transition"
                  >
                    Mark Resolved
                  </button>
                )}
              </div>
            </div>
          </div>
        ))}
      </div>

      {/* Submit Report Modal */}
      {showModal && (
        <div className="fixed inset-0 z-[2000] flex items-center justify-center p-4 bg-black/80 backdrop-blur-sm">
          <div className="w-full max-w-lg bg-[#0E131F] border border-slate-700 rounded-3xl p-6 shadow-2xl space-y-4">
            <div className="flex items-center justify-between border-b border-slate-800 pb-3">
              <h3 className="text-sm font-bold text-white">File Field Incident Report</h3>
              <button onClick={() => setShowModal(false)} className="text-slate-400 hover:text-white">
                <X className="w-5 h-5" />
              </button>
            </div>

            <form onSubmit={handleSubmit} className="space-y-3 text-xs">
              <div>
                <label className="block text-slate-400 mb-1">Reporter Name / Agency</label>
                <input
                  type="text"
                  value={formData.reporter}
                  onChange={(e) => setFormData({ ...formData, reporter: e.target.value })}
                  className="w-full bg-slate-900 border border-slate-700 rounded-xl px-3 py-2 text-white"
                  required
                />
              </div>

              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-slate-400 mb-1">Incident Type</label>
                  <select
                    value={formData.type}
                    onChange={(e) => setFormData({ ...formData, type: e.target.value })}
                    className="w-full bg-slate-900 border border-slate-700 rounded-xl px-3 py-2 text-white"
                  >
                    <option value="LANDSLIDE">LANDSLIDE</option>
                    <option value="FLOOD">FLOOD</option>
                    <option value="ROAD_BLOCKED">ROAD_BLOCKED</option>
                    <option value="DAMAGED_ROAD">DAMAGED_ROAD</option>
                    <option value="HEAVY_TRAFFIC">HEAVY_TRAFFIC</option>
                    <option value="BRIDGE_DAMAGE">BRIDGE_DAMAGE</option>
                  </select>
                </div>
                <div>
                  <label className="block text-slate-400 mb-1">Severity</label>
                  <select
                    value={formData.severity}
                    onChange={(e) => setFormData({ ...formData, severity: e.target.value })}
                    className="w-full bg-slate-900 border border-slate-700 rounded-xl px-3 py-2 text-white font-mono"
                  >
                    <option value="CRITICAL">CRITICAL</option>
                    <option value="HIGH">HIGH</option>
                    <option value="MEDIUM">MEDIUM</option>
                    <option value="LOW">LOW</option>
                  </select>
                </div>
              </div>

              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-slate-400 mb-1">Latitude</label>
                  <input
                    type="number"
                    step="0.0001"
                    value={formData.latitude}
                    onChange={(e) => setFormData({ ...formData, latitude: parseFloat(e.target.value) })}
                    className="w-full bg-slate-900 border border-slate-700 rounded-xl px-3 py-2 text-white font-mono"
                    required
                  />
                </div>
                <div>
                  <label className="block text-slate-400 mb-1">Longitude</label>
                  <input
                    type="number"
                    step="0.0001"
                    value={formData.longitude}
                    onChange={(e) => setFormData({ ...formData, longitude: parseFloat(e.target.value) })}
                    className="w-full bg-slate-900 border border-slate-700 rounded-xl px-3 py-2 text-white font-mono"
                    required
                  />
                </div>
              </div>

              <div>
                <label className="block text-slate-400 mb-1">Incident Description</label>
                <textarea
                  rows={3}
                  value={formData.description}
                  onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                  className="w-full bg-slate-900 border border-slate-700 rounded-xl px-3 py-2 text-white"
                  required
                />
              </div>

              <button
                type="submit"
                disabled={loading}
                className="w-full py-2.5 mt-2 rounded-xl bg-amber-600 hover:bg-amber-500 font-bold text-white transition"
              >
                {loading ? 'Submitting Report...' : 'Publish Field Incident'}
              </button>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
