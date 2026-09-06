import React, { useState } from 'react';
import {
  ScrollText,
  Plus,
  AlertOctagon,
  ExternalLink,
  Calendar,
  CheckCircle2,
  X
} from 'lucide-react';
import { createAdvisory } from '../services/api';
import CustomSelect from './CustomSelect';

export default function AdvisoriesView({ advisories = [], onAdvisoriesUpdated }) {
  const [showModal, setShowModal] = useState(false);
  const [loading, setLoading] = useState(false);
  const [notice, setNotice] = useState(null);

  const [formData, setFormData] = useState({
    title: 'NH-6 Heavy Freight Diversion Advisory',
    description: 'Meghalaya Police advises heavy trucks to divert via Lumding due to Sonapur ghat stabilization works.',
    type: 'ROAD_CLOSURE',
    severity: 'HIGH',
    source: 'Meghalaya Police & MSDMA',
    sourceUrl: 'https://msdma.gov.in/advisories',
    validFrom: new Date().toISOString().slice(0, 16),
    validUntil: new Date(Date.now() + 86400000 * 2).toISOString().slice(0, 16)
  });

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      setLoading(true);
      await createAdvisory(formData);
      setShowModal(false);
      setNotice('Official advisory published successfully!');
      if (onAdvisoriesUpdated) onAdvisoriesUpdated();
    } catch (err) {
      console.error(err);
      setNotice(`Failed to post advisory: ${err.message}`);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-6 space-y-6 max-w-[1700px] mx-auto">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 border-b border-slate-800 pb-4">
        <div>
          <h2 className="text-base font-bold text-white flex items-center gap-2">
            Official Highway & Disaster Advisories
          </h2>
          <p className="text-xs text-slate-400">
            Government disaster management bulletins, police traffic diversions, and weather warnings.
          </p>
        </div>

        <button
          onClick={() => setShowModal(true)}
          className="px-4 py-2 rounded-xl bg-indigo-600 hover:bg-indigo-500 text-white font-bold text-xs flex items-center gap-2 shadow-lg shadow-indigo-950/40 transition"
        >
          <Plus className="w-4 h-4" />
          <span>Publish Advisory</span>
        </button>
      </div>

      {notice && (
        <div className="p-3.5 rounded-xl bg-emerald-500/10 border border-emerald-500/30 text-emerald-300 text-xs flex items-center gap-2 font-medium">
          <CheckCircle2 className="w-4 h-4 text-emerald-400 shrink-0" />
          <span>{notice}</span>
        </div>
      )}

      {/* Advisories Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {advisories.map((a) => (
          <div
            key={a.id}
            className="p-5 rounded-2xl bg-[#0E131F]/90 border border-slate-800 shadow-xl space-y-3"
          >
            <div className="flex items-start justify-between gap-3">
              <div className="flex items-center gap-2">
                <span className="text-[10px] font-mono px-2 py-0.5 rounded font-bold bg-amber-500/20 text-amber-300 border border-amber-500/30">
                  {a.type}
                </span>
                <span className={`text-[10px] font-mono px-2 py-0.5 rounded font-bold ${a.severity === 'CRITICAL' ? 'bg-red-500/20 text-red-300' : 'bg-slate-800 text-slate-300'
                  }`}>
                  {a.severity}
                </span>
              </div>
              <span className="text-[10px] text-emerald-400 font-semibold px-2 py-0.5 rounded bg-emerald-500/10 border border-emerald-500/20">
                ACTIVE
              </span>
            </div>

            <h3 className="text-sm font-bold text-white">{a.title}</h3>
            <p className="text-xs text-slate-300 leading-relaxed">{a.description}</p>

            <div className="pt-3 border-t border-slate-800/80 text-[11px] text-slate-400 flex flex-wrap items-center justify-between gap-2">
              <div>
                <span>Source: <strong className="text-slate-200">{a.source}</strong></span>
              </div>
              {a.sourceUrl && (
                <a
                  href={a.sourceUrl}
                  target="_blank"
                  rel="noreferrer"
                  className="flex items-center gap-1 text-cyan-400 hover:text-cyan-300 font-medium"
                >
                  <span>Verify Bulletin</span>
                  <ExternalLink className="w-3 h-3" />
                </a>
              )}
            </div>
          </div>
        ))}
      </div>

      {/* Publish Advisory Modal */}
      {showModal && (
        <div className="fixed inset-0 z-[2000] flex items-center justify-center p-4 bg-black/80 backdrop-blur-sm">
          <div className="w-full max-w-lg bg-[#0E131F] border border-slate-700 rounded-3xl p-6 shadow-2xl space-y-4">
            <div className="flex items-center justify-between border-b border-slate-800 pb-3">
              <h3 className="text-sm font-bold text-white">Publish Official Road Advisory</h3>
              <button onClick={() => setShowModal(false)} className="text-slate-400 hover:text-white">
                <X className="w-5 h-5" />
              </button>
            </div>

            <form onSubmit={handleSubmit} className="space-y-3 text-xs">
              <div>
                <label className="block text-slate-400 mb-1">Advisory Title</label>
                <input
                  type="text"
                  value={formData.title}
                  onChange={(e) => setFormData({ ...formData, title: e.target.value })}
                  className="w-full bg-slate-900 border border-slate-700 rounded-xl px-3 py-2 text-white"
                  required
                />
              </div>

              <div>
                <label className="block text-slate-400 mb-1">Issuing Authority / Agency</label>
                <input
                  type="text"
                  value={formData.source}
                  onChange={(e) => setFormData({ ...formData, source: e.target.value })}
                  className="w-full bg-slate-900 border border-slate-700 rounded-xl px-3 py-2 text-white"
                  required
                />
              </div>

              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-slate-400 mb-1">Advisory Classification</label>
                  <CustomSelect
                    value={formData.type}
                    onChange={(value) => setFormData({ ...formData, type: value })}
                    options={['ROAD_CLOSURE', 'TRAFFIC_ADVISORY', 'SECURITY_ADVISORY', 'DIVERSION', 'WEATHER_ADVISORY'].map(value => ({ value, label: value }))}
                    ariaLabel="Advisory classification"
                  />
                </div>
                <div>
                  <label className="block text-slate-400 mb-1">Severity</label>
                  <CustomSelect
                    value={formData.severity}
                    onChange={(value) => setFormData({ ...formData, severity: value })}
                    options={['CRITICAL', 'HIGH', 'MEDIUM'].map(value => ({ value, label: value }))}
                    ariaLabel="Advisory severity"
                  />
                </div>
              </div>

              <div>
                <label className="block text-slate-400 mb-1">Advisory Description & Instructions</label>
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
                className="w-full py-2.5 mt-2 rounded-xl bg-indigo-600 hover:bg-indigo-500 font-bold text-white transition"
              >
                {loading ? 'Publishing...' : 'Broadcast Official Advisory'}
              </button>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
