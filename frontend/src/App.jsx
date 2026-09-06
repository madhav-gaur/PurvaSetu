import { useState, useEffect, useCallback } from 'react';
import Navbar from './components/Navbar';
import Sidebar from './components/Sidebar';
import DashboardView from './components/DashboardView';
import LeafletMap from './components/LeafletMap';
import RouteComparisonView from './components/RouteComparisonView';
import VehiclesView from './components/VehiclesView';
import ShipmentsView from './components/ShipmentsView';
import RoadReportsView from './components/RoadReportsView';
import RiskHazardsView from './components/RiskHazardsView';
import AdvisoriesView from './components/AdvisoriesView';
import AnalyticsView from './components/AnalyticsView';
import SimulationPanel from './components/SimulationPanel';
import EmergencyDemoModal from './components/EmergencyDemoModal';
import { GlassCard } from 'react-glass-ui';

import {
  getDashboardSummary,
  getAnalytics,
  getWeather,
  getRoutes,
  getVehicles,
  getShipments,
  getRoadReports,
  getHazards,
  getAdvisories,
  getAlerts,
  acknowledgeAlert,
  calculateRoutes
} from './services/api';
import { DEFAULT_DESTINATION, DEFAULT_ORIGIN } from './constants/routeLocations';

const toCollection = (value) => {
  if (Array.isArray(value)) return value;
  if (Array.isArray(value?.data)) return value.data;
  if (Array.isArray(value?.content)) return value.content;
  return [];
};

export default function App() {
  const [currentTab, setTab] = useState('dashboard');
  const [loading, setLoading] = useState(true);
  const [theme, setTheme] = useState(() => localStorage.getItem('ner-theme') || 'dark');

  useEffect(() => {
    document.documentElement.dataset.theme = theme;
    localStorage.setItem('ner-theme', theme);
  }, [theme]);

  // Core Data States
  const [summary, setSummary] = useState({});
  const [analytics, setAnalytics] = useState({});
  const [weather, setWeather] = useState(null);
  const [routes, setRoutes] = useState(null);
  const [routesLoading, setRoutesLoading] = useState(false);
  const [lastUpdated, setLastUpdated] = useState(null);
  const [vehicles, setVehicles] = useState([]);
  const [shipments, setShipments] = useState([]);
  const [reports, setReports] = useState([]);
  const [hazards, setHazards] = useState([]);
  const [advisories, setAdvisories] = useState([]);
  const [alerts, setAlerts] = useState([]);
  const [routeSelection, setRouteSelection] = useState({
    origin: DEFAULT_ORIGIN,
    destination: DEFAULT_DESTINATION,
  });

  // Modals
  const [isEmergencyDemoOpen, setIsEmergencyDemoOpen] = useState(false);
  const [isSimulationModalOpen, setIsSimulationModalOpen] = useState(false);
  const [isSidebarCollapsed, setIsSidebarCollapsed] = useState(false);
  const [isMobileNavOpen, setIsMobileNavOpen] = useState(false);

  // Active weather location
  const [weatherLocation, setWeatherLocation] = useState({
    lat: 26.1445,
    lon: 91.7362,
    name: 'Guwahati Logistics Hub'
  });

  // Fetch initial data
  const loadAllData = useCallback(async () => {
    setRoutesLoading(true);
    try {
      const [
        sumData,
        anaData,
        weaData,
        vehData,
        shipData,
        repData,
        hazData,
        advData,
        altData,
        routeData
      ] = await Promise.all([
        getDashboardSummary().catch(() => ({})),
        getAnalytics().catch(() => ({})),
        getWeather(weatherLocation.lat, weatherLocation.lon, weatherLocation.name).catch(() => null),
        getVehicles().catch(() => []),
        getShipments().catch(() => []),
        getRoadReports().catch(() => []),
        getHazards().catch(() => []),
        getAdvisories().catch(() => []),
        getAlerts().catch(() => []),
        getRoutes().catch(() => null)
      ]);

      setSummary(sumData);
      setAnalytics(anaData);
      setWeather(weaData);
      setVehicles(toCollection(vehData));
      setShipments(toCollection(shipData));
      setReports(toCollection(repData));
      setHazards(toCollection(hazData));
      setAdvisories(toCollection(advData));
      setAlerts(toCollection(altData));
      setRoutes(routeData);
      setLastUpdated(new Date());
    } catch (err) {
      console.error('Failed to load platform data', err);
    } finally {
      setRoutesLoading(false);
      setLoading(false);
    }
  }, [weatherLocation]);

  useEffect(() => {
    // eslint-disable-next-line react-hooks/set-state-in-effect
    loadAllData();
    // Poll updates every 15 seconds
    const interval = setInterval(loadAllData, 15000);
    return () => clearInterval(interval);
  }, [loadAllData]);

  // Handlers
  const handleAcknowledgeAlert = async (id) => {
    try {
      await acknowledgeAlert(id);
      setAlerts(prev => prev.filter(a => a.id !== id));
      loadAllData();
    } catch (err) {
      console.error(err);
    }
  };

  const handleSelectWeatherLocation = async (lat, lon, name) => {
    setWeatherLocation({ lat, lon, name });
    try {
      const w = await getWeather(lat, lon, name);
      setWeather(w);
    } catch (err) {
      console.error(err);
    }
  };

  const handleDemoUpdate = (demoData) => {
    if (demoData?.vehicle) {
      setVehicles(prev => prev.map(v => v.id === demoData.vehicle.id ? demoData.vehicle : v));
    }
    if (demoData?.currentRoute) {
      setRoutes(prev => ({
        ...prev,
        recommendedRoute: demoData.currentRoute,
        alternativeRoutes: demoData.alternateRoute ? [demoData.alternateRoute] : prev?.alternativeRoutes
      }));
    }
    loadAllData();
  };

  const handleRouteSelectionChange = async (origin, destination) => {
    setRouteSelection({ origin, destination });
    setRoutesLoading(true);
    try {
      const routeData = await calculateRoutes({
        startLatitude: origin.lat,
        startLongitude: origin.lng,
        destinationLatitude: destination.lat,
        destinationLongitude: destination.lng,
        originName: origin.name,
        destinationName: destination.name,
        priority: 'CRITICAL',
      });
      setRoutes(routeData);
      setLastUpdated(new Date());
    } catch (err) {
      console.error('Failed to update global route selection', err);
    } finally {
      setRoutesLoading(false);
    }
  };

  const sidebarCounts = {
    activeVehicles: vehicles.length,
    criticalShipments: shipments.filter(s => s.priority === 'CRITICAL').length,
    pendingReports: reports.filter(r => r.status === 'PENDING').length,
    activeAdvisories: advisories.length,
  };

  if (loading && !weather) {
    return (
      <div className="min-h-screen bg-[#0B0F19] text-white flex flex-col items-center justify-center space-y-4">
        <div className="w-12 h-12 border-4 border-emerald-500 border-t-transparent rounded-full animate-spin"></div>
        <p className="text-sm font-semibold tracking-wide text-slate-300">
          Connecting to PurvaSetu Logistics Engine & Open-Meteo Telemetry...
        </p>
      </div>
    );
  }

  return (
    <div className="app-shell relative isolate min-h-screen text-slate-100 flex flex-col font-sans selection:bg-emerald-500 selection:text-white">
      {theme === 'fluid-glass' && (
        <GlassCard
          className="fluid-glass-layer"
          width={1600}
          height={1000}
          blur={2}
          distortion={24}
          chromaticAberration={8}
          brightness={92}
          saturation={118}
          borderRadius={0}
          borderSize={0}
          backgroundColor="#0b2230"
          backgroundOpacity={0.2}
          innerLightColor="#67e8f9"
          innerLightOpacity={0.18}
          outerLightColor="#14b8a6"
          outerLightOpacity={0.12}
          avoidSvgCreation={false}
        />
      )}
      {/* Top Navigation */}
      <Navbar
        theme={theme}
        onThemeChange={setTheme}
        activeAlertsCount={alerts.length}
        activeAlerts={alerts}
        onAcknowledgeAlert={handleAcknowledgeAlert}
        routeSelection={routeSelection}
        onRouteSelectionChange={handleRouteSelectionChange}
        onOpenEmergencyDemo={() => setIsEmergencyDemoOpen(true)}
        onOpenSimulation={() => setIsSimulationModalOpen(true)}
        onOpenMobileNav={() => setIsMobileNavOpen((prev) => !prev)}
        aiSource={routes?.recommendedRoute?.predictionSource || 'FASTAPI_RANDOM_FOREST_ML'}
      />

      {/* Main Content Layout */}
      <div className="flex flex-1 overflow-hidden">
        {/* Navigation Sidebar */}
        <Sidebar
          currentTab={currentTab}
          setTab={setTab}
          counts={sidebarCounts}
          collapsed={isSidebarCollapsed}
          onToggle={() => setIsSidebarCollapsed(prev => !prev)}
          mobileOpen={isMobileNavOpen}
          onMobileClose={() => setIsMobileNavOpen(false)}
        />

        {/* View Content Body */}
        <main className={`app-main-surface relative z-10 min-w-0 flex-1 overflow-y-auto transition-[margin] duration-200 ${isSidebarCollapsed ? 'lg:ml-16' : 'lg:ml-64'}`}>
          {currentTab === 'dashboard' && (
            <DashboardView
              summary={summary}
              routes={routes}
              weather={weather}
              vehicles={vehicles}
              reports={reports}
              hazards={hazards}
              advisories={advisories}
              alerts={alerts}
              lastUpdated={lastUpdated}
              routesLoading={routesLoading}
              onAcknowledgeAlert={handleAcknowledgeAlert}
              onSelectWeatherLocation={handleSelectWeatherLocation}
              onViewRoutesTab={() => setTab('routes')}
            />
          )}

          {currentTab === 'map' && (
            <div className="p-6 space-y-4 max-w-[1700px] mx-auto">
              <div className="flex items-center justify-between">
                <div>
                  <h2 className="text-base font-bold text-white">Full-Screen Interactive Disaster & Logistics Map</h2>
                  <p className="text-xs text-slate-400">Live corridor visualization with GPS vehicle pings and hazard layers</p>
                </div>
              </div>
              <LeafletMap
                primaryRoute={routes?.recommendedRoute}
                alternativeRoute={routes?.alternativeRoutes?.[0]}
                vehicles={vehicles}
                hazards={hazards}
                reports={reports}
                originName={routes?.origin}
                destinationName={routes?.destination}
                height="80vh"
              />
            </div>
          )}

          {currentTab === 'routes' && (
            <RouteComparisonView
              routeData={routes}
              onRoutesCalculated={(res) => setRoutes(res)}
              routeSelection={routeSelection}
              onRouteSelectionChange={handleRouteSelectionChange}
              vehicles={vehicles}
              shipments={shipments}
            />
          )}

          {currentTab === 'vehicles' && (
            <VehiclesView
              vehicles={vehicles}
              onVehicleUpdated={loadAllData}
            />
          )}

          {currentTab === 'shipments' && (
            <ShipmentsView
              shipments={shipments}
              vehicles={vehicles}
              onShipmentsUpdated={loadAllData}
            />
          )}

          {currentTab === 'reports' && (
            <RoadReportsView
              reports={reports}
              onReportsUpdated={loadAllData}
            />
          )}

          {currentTab === 'hazards' && (
            <RiskHazardsView
              hazards={hazards}
            />
          )}

          {currentTab === 'advisories' && (
            <AdvisoriesView
              advisories={advisories}
              onAdvisoriesUpdated={loadAllData}
            />
          )}

          {currentTab === 'analytics' && (
            <AnalyticsView
              analytics={analytics}
            />
          )}

          {currentTab === 'simulation' && (
            <div className="p-6 max-w-4xl mx-auto">
              <SimulationPanel
                onScenarioTriggered={() => loadAllData()}
              />
            </div>
          )}
        </main>
      </div>

      {/* Emergency Demo Modal (SIH Primary Demonstration) */}
      <EmergencyDemoModal
        isOpen={isEmergencyDemoOpen}
        onClose={() => setIsEmergencyDemoOpen(false)}
        onDemoUpdate={handleDemoUpdate}
      />

      {/* Quick Simulation Modal */}
      {isSimulationModalOpen && (
        <div className="fixed inset-0 z-[2000] flex items-center justify-center p-4 bg-black/80 backdrop-blur-sm">
          <div className="w-full max-w-3xl">
            <SimulationPanel
              onScenarioTriggered={() => {
                loadAllData();
                setIsSimulationModalOpen(false);
              }}
              onClose={() => setIsSimulationModalOpen(false)}
            />
          </div>
        </div>
      )}
    </div>
  );
}
