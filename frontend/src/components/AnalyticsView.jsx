import React from 'react';
import {
  BarChart3,
  TrendingUp,
  ShieldCheck,
  Clock,
  Truck,
  Layers
} from 'lucide-react';
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
  ResponsiveContainer,
  PieChart,
  Pie,
  Cell,
  LineChart,
  Line,
  Legend
} from 'recharts';

export default function AnalyticsView({ analytics = {} }) {
  const monthlyData = analytics.monthlyDisruptions || [
    { month: 'Apr', incidents: 14, rainfall: 120 },
    { month: 'May', incidents: 28, rainfall: 260 },
    { month: 'Jun', incidents: 65, rainfall: 480 },
    { month: 'Jul', incidents: 82, rainfall: 590 },
    { month: 'Aug', incidents: 74, rainfall: 510 },
    { month: 'Sep', incidents: 38, rainfall: 320 }
  ];

  const corridorData = analytics.corridorRiskScores || [
    { corridor: 'NH-6 (Meghalaya)', risk: 78 },
    { corridor: 'NH-27 (Bypass)', risk: 24 },
    { corridor: 'NH-29 (Dimapur)', risk: 62 },
    { corridor: 'NH-102 (Imphal)', risk: 38 },
    { corridor: 'NH-10 (Sikkim)', risk: 84 }
  ];

  const hazardData = analytics.hazardTypeDistribution || [
    { type: 'Landslide', count: 46 },
    { type: 'Flash Flood', count: 32 },
    { type: 'Bridge Damage', count: 12 },
    { type: 'Road Erosion', count: 24 }
  ];

  const deliveryData = analytics.deliveryOnTimeStats || [
    { name: 'Standard On-Time', value: 68 },
    { name: 'Safely Rerouted On-Time', value: 24 },
    { name: 'Delayed by Weather', value: 8 }
  ];

  const COLORS = ['#10B981', '#3B82F6', '#EF4444', '#F59E0B', '#8B5CF6'];

  return (
    <div className="p-6 space-y-6 max-w-[1700px] mx-auto">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 border-b border-slate-800 pb-4">
        <div>
          <h2 className="text-base font-bold text-white flex items-center gap-2">
            Disaster Analytics & Corridor Reliability Intelligence
          </h2>
          <p className="text-xs text-slate-400">
            Historical vulnerability trends, weather correlations, and dynamic reroute effectiveness.
          </p>
        </div>

        {/* Highlight metric cards */}
        <div className="flex items-center gap-3">
          <div className="px-4 py-2 rounded-xl bg-slate-900 border border-slate-800 text-xs">
            <span className="text-slate-400 block text-[10px]">Avg Time Saved via Reroute:</span>
            <span className="font-bold text-emerald-400 text-sm">
              {analytics.averageRerouteBypassTimeSavedMinutes || 145} mins
            </span>
          </div>
          <div className="px-4 py-2 rounded-xl bg-slate-900 border border-slate-800 text-xs">
            <span className="text-slate-400 block text-[10px]">Critical Cargo Protected:</span>
            <span className="font-bold text-cyan-400 text-sm">
              {analytics.totalCargoProtectedTons || 1280.5} Tons
            </span>
          </div>
        </div>
      </div>

      {/* Analytics Charts Grid */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Monthly Disruptions & Rainfall */}
        <div className="p-5 rounded-2xl bg-[#0E131F]/90 border border-slate-800 shadow-xl space-y-4">
          <div className="flex items-center justify-between border-b border-slate-800 pb-3">
            <h3 className="text-xs font-bold uppercase tracking-wider text-slate-300">
              Monsoon Rainfall vs Road Disruptions Trend
            </h3>
            <span className="text-[10px] text-cyan-400 font-mono">Telemetry Correlation</span>
          </div>
          <div className="h-64 w-full">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={monthlyData}>
                <XAxis dataKey="month" stroke="#64748B" fontSize={11} />
                <YAxis stroke="#64748B" fontSize={11} />
                <Tooltip contentStyle={{ backgroundColor: '#111827', borderColor: '#374151', borderRadius: '8px', fontSize: '11px', color: '#F3F4F6' }} labelStyle={{ color: '#CBD5E1' }} itemStyle={{ color: '#F3F4F6' }} />
                <Legend wrapperStyle={{ fontSize: '11px', paddingTop: '10px' }} />
                <Bar dataKey="incidents" name="Reported Disruptions" fill="#EF4444" radius={[4, 4, 0, 0]} />
                <Bar dataKey="rainfall" name="Rainfall (mm)" fill="#0284C7" radius={[4, 4, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </div>

        {/* Corridor Risk Ranking */}
        <div className="p-5 rounded-2xl bg-[#0E131F]/90 border border-slate-800 shadow-xl space-y-4">
          <div className="flex items-center justify-between border-b border-slate-800 pb-3">
            <h3 className="text-xs font-bold uppercase tracking-wider text-slate-300">
              Corridor Hazard Vulnerability Index (%)
            </h3>
            <span className="text-[10px] text-amber-400 font-mono">Risk Scoring</span>
          </div>
          <div className="h-64 w-full">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={corridorData} layout="vertical">
                <XAxis type="number" domain={[0, 100]} stroke="#64748B" fontSize={11} />
                <YAxis dataKey="corridor" type="category" width={110} stroke="#64748B" fontSize={11} />
                <Tooltip contentStyle={{ backgroundColor: '#111827', borderColor: '#374151', borderRadius: '8px', fontSize: '11px', color: '#F3F4F6' }} labelStyle={{ color: '#CBD5E1' }} itemStyle={{ color: '#F3F4F6' }} />
                <Bar dataKey="risk" name="Vulnerability %" fill="#F59E0B" radius={[0, 4, 4, 0]}>
                  {corridorData.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={entry.risk > 60 ? '#EF4444' : entry.risk > 30 ? '#F59E0B' : '#10B981'} />
                  ))}
                </Bar>
              </BarChart>
            </ResponsiveContainer>
          </div>
        </div>

        {/* Hazard Type Distribution */}
        <div className="p-5 rounded-2xl bg-[#0E131F]/90 border border-slate-800 shadow-xl space-y-4">
          <div className="flex items-center justify-between border-b border-slate-800 pb-3">
            <h3 className="text-xs font-bold uppercase tracking-wider text-slate-300">
              Disaster Incident Types Breakdown
            </h3>
            <span className="text-[10px] text-slate-400 font-mono">Field Reports</span>
          </div>
          <div className="h-64 w-full flex items-center justify-center">
            <ResponsiveContainer width="100%" height="100%">
              <PieChart>
                <Pie
                  data={hazardData}
                  cx="50%"
                  cy="50%"
                  outerRadius={80}
                  dataKey="count"
                  nameKey="type"
                  label={({ name, percent }) => `${name} (${(percent * 100).toFixed(0)}%)`}
                  labelLine={false}
                >
                  {hazardData.map((entry, index) => (
                    <Cell key={`pie-${index}`} fill={COLORS[index % COLORS.length]} />
                  ))}
                </Pie>
                <Tooltip contentStyle={{ backgroundColor: '#111827', borderColor: '#374151', borderRadius: '8px', fontSize: '11px', color: '#F3F4F6' }} labelStyle={{ color: '#CBD5E1' }} itemStyle={{ color: '#F3F4F6' }} />
              </PieChart>
            </ResponsiveContainer>
          </div>
        </div>

        {/* Delivery On-Time & Safety Stats */}
        <div className="p-5 rounded-2xl bg-[#0E131F]/90 border border-slate-800 shadow-xl space-y-4">
          <div className="flex items-center justify-between border-b border-slate-800 pb-3">
            <h3 className="text-xs font-bold uppercase tracking-wider text-slate-300">
              Life-Saving Consignment Delivery Performance
            </h3>
            <span className="text-[10px] text-emerald-400 font-mono">92% Mission Success</span>
          </div>
          <div className="h-64 w-full flex items-center justify-center">
            <ResponsiveContainer width="100%" height="100%">
              <PieChart>
                <Pie
                  data={deliveryData}
                  cx="50%"
                  cy="50%"
                  innerRadius={50}
                  outerRadius={80}
                  dataKey="value"
                  paddingAngle={5}
                >
                  <Cell fill="#10B981" />
                  <Cell fill="#3B82F6" />
                  <Cell fill="#EF4444" />
                </Pie>
                <Tooltip contentStyle={{ backgroundColor: '#111827', borderColor: '#374151', borderRadius: '8px', fontSize: '11px', color: '#F3F4F6' }} labelStyle={{ color: '#CBD5E1' }} itemStyle={{ color: '#F3F4F6' }} />
                <Legend wrapperStyle={{ fontSize: '11px', paddingTop: '10px' }} />
              </PieChart>
            </ResponsiveContainer>
          </div>
        </div>
      </div>
    </div>
  );
}
