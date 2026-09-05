import { useEffect, useRef, useState } from 'react';
import {
  Cpu,
  PlayCircle,
  Bell,
  Sparkles,
  AlertTriangle,
  CheckCheck,
  X,
  Menu,
  ChevronUp,
  MapPinned,
  Moon,
  Sun,
} from 'lucide-react';

import { ROUTE_LOCATIONS } from '../constants/routeLocations';
import logoUrl from '../../assets/logo.png';

export default function Navbar({
  theme = 'dark',
  onThemeChange,
  activeAlertsCount = 0,
  activeAlerts = [],
  onAcknowledgeAlert,
  routeSelection,
  onRouteSelectionChange,
  onOpenEmergencyDemo,
  onOpenSimulation,
  onOpenMobileNav,
  aiSource = 'FASTAPI_RANDOM_FOREST_ML',
}) {
  const [isNotificationsOpen, setIsNotificationsOpen] = useState(false);
  const [isNavbarOpen, setIsNavbarOpen] = useState(false);
  const notificationRef = useRef(null);

  useEffect(() => {
    const closeOnOutsideClick = (event) => {
      if (
        notificationRef.current &&
        !notificationRef.current.contains(event.target)
      ) {
        setIsNotificationsOpen(false);
      }
    };

    const closeOnEscape = (event) => {
      if (event.key === 'Escape') {
        setIsNotificationsOpen(false);
      }
    };

    document.addEventListener('mousedown', closeOnOutsideClick);
    document.addEventListener('keydown', closeOnEscape);

    return () => {
      document.removeEventListener('mousedown', closeOnOutsideClick);
      document.removeEventListener('keydown', closeOnEscape);
    };
  }, []);

  const origin =
    routeSelection?.origin || ROUTE_LOCATIONS[0];

  const destination =
    routeSelection?.destination || ROUTE_LOCATIONS[1];

  const isAiModel = aiSource.includes('FASTAPI');
  const themeOptions = [
    { id: 'light', label: 'Light', description: 'Bright workspace', icon: Sun },
    { id: 'dark', label: 'Dark', description: 'Default dark workspace', icon: Moon },
    { id: 'midnight', label: 'Midnight Blue', description: 'Deep blue contrast', icon: Moon },
    { id: 'charcoal', label: 'Graphite', description: 'Neutral dark contrast', icon: Moon },
  ];

  const handleOriginChange = (event) => {
    const selectedOrigin = ROUTE_LOCATIONS.find(
      (location) => location.name === event.target.value
    );

    if (selectedOrigin && onRouteSelectionChange) {
      onRouteSelectionChange(selectedOrigin, destination);
    }
  };

  const handleDestinationChange = (event) => {
    const selectedDestination = ROUTE_LOCATIONS.find(
      (location) => location.name === event.target.value
    );

    if (selectedDestination && onRouteSelectionChange) {
      onRouteSelectionChange(origin, selectedDestination);
    }
  };

  return (
    <header className="sticky top-0 z-[100] w-full border-b border-slate-800/80 bg-[#0E131F]/95 px-3 py-2 shadow-lg shadow-black/20 backdrop-blur-md sm:px-4 lg:px-6">
      <div className="relative flex min-h-12 w-full items-center gap-2 py-1 lg:min-h-[128px] lg:flex-col">
        {/* BRAND */}
        <div className="relative flex min-h-12 min-w-0 flex-1 shrink-0 items-center justify-between gap-3 lg:w-full lg:flex-none lg:justify-center">
          <div className="flex h-10 w-10 shrink-0 items-center justify-center rounded-xl bg-gradient-to-tr from-emerald-600 via-teal-500 to-cyan-400 p-0.5 shadow-md shadow-emerald-950/50 lg:absolute lg:left-0">
            <div className="flex h-full w-full items-center justify-center rounded-[10px] bg-[#0E131F]">
              <img
                src={logoUrl}
                alt="PurvaSetu logo"
                className="h-full w-full rounded-[9px] object-cover"
              />
            </div>
          </div>

          <div className="absolute left-1/2 min-w-0 -translate-x-1/2">
            <div className="flex items-center gap-2">
              <h1 className="whitespace-nowrap text-xs font-bold tracking-tight text-white sm:text-sm lg:text-base">
                PURVASETU
              </h1>

              <span className="hidden shrink-0 rounded border border-emerald-500/30 bg-emerald-500/20 px-2 py-0.5 font-mono text-[9px] uppercase text-emerald-300 2xl:inline">
                SIH PROTOTYPE
              </span>
            </div>

            <p className="hidden text-[11px] font-medium text-slate-400 2xl:block">
              AI-Based Disaster Accessibility & Dynamic Rerouting
            </p>
          </div>

        </div>

        <div className="flex w-auto min-w-0 flex-none items-center gap-3 lg:w-full lg:flex-1">
          {/* ROUTE SELECTOR */}
          <div className="hidden w-full min-w-0 flex-1 items-center gap-2 lg:flex">
            <div className="hidden min-w-0 flex-1 items-center lg:flex">
              <div className="flex min-w-0 w-full max-w-[620px] items-center gap-2 rounded-2xl border border-slate-700/70 bg-slate-900/50 p-1.5">

                <select
                  aria-label="Global origin terminal"
                  value={origin?.name || ''}
                  onChange={handleOriginChange}
                  className="min-w-0 flex-1 truncate rounded-xl border border-slate-700 bg-[#141C2B] px-3 py-2.5 text-[11px] text-slate-200 outline-none focus:border-emerald-500"
                >
                  {ROUTE_LOCATIONS.map((location) => (
                    <option
                      key={location.name}
                      value={location.name}
                    >
                      {location.name}
                    </option>
                  ))}
                </select>

                <span className="shrink-0 px-1 text-xs text-slate-500">
                  →
                </span>

                <select
                  aria-label="Global destination terminal"
                  value={destination?.name || ''}
                  onChange={handleDestinationChange}
                  className="min-w-0 flex-1 truncate rounded-xl border border-slate-700 bg-[#141C2B] px-3 py-2.5 text-[11px] text-slate-200 outline-none focus:border-cyan-500"
                >
                  {ROUTE_LOCATIONS.map((location) => (
                    <option
                      key={location.name}
                      value={location.name}
                    >
                      {location.name}
                    </option>
                  ))}
                </select>
              </div>
            </div>
          </div>

          {/* ENGINE + WEATHER — OUTSIDE ROUTE */}
          <div className="hidden shrink-0 items-center gap-2 2xl:flex">
            <div className="flex items-center gap-2 rounded-xl border border-slate-700/60 bg-slate-800/60 px-3 py-2 text-[11px]">
              <span className="relative flex h-2 w-2">
                <span className="absolute inline-flex h-full w-full animate-ping rounded-full bg-emerald-400 opacity-60" />
                <span className="relative inline-flex h-2 w-2 rounded-full bg-emerald-400" />
              </span>

              <Cpu className="h-3.5 w-3.5 text-emerald-400" />

              <div className="leading-tight">
                <p className="text-[8px] uppercase text-slate-500">
                  Engine
                </p>
                <p className="font-semibold text-emerald-300">
                  {isAiModel ? 'AI Model Active' : 'Fallback Active'}
                </p>
              </div>
            </div>


          </div>

          {/* RIGHT ACTIONS */}
          <div className="ml-auto flex shrink-0 items-center gap-1 sm:gap-2">

            <div className="flex items-center gap-1 lg:hidden">
              <button
                type="button"
                onClick={() => setIsNavbarOpen((prev) => !prev)}
                className="flex h-10 w-10 shrink-0 items-center justify-center rounded-xl border border-slate-600 bg-slate-800/80 text-slate-300 transition hover:border-cyan-400/50 hover:bg-slate-700 hover:text-white"
                title={isNavbarOpen ? 'Hide location selector' : 'Show location selector'}
                aria-label={isNavbarOpen ? 'Hide location selector' : 'Show location selector'}
                aria-expanded={isNavbarOpen}
              >
                {isNavbarOpen ? <ChevronUp className="h-4 w-4" /> : <MapPinned className="h-4 w-4" />}
              </button>

              <button
                type="button"
                onClick={onOpenMobileNav}
                className="flex h-10 w-10 shrink-0 items-center justify-center rounded-xl border border-slate-600 bg-slate-800/80 text-slate-300 transition hover:border-cyan-400/50 hover:bg-slate-700 hover:text-white"
                title="Open navigation menu"
                aria-label="Open navigation menu"
              >
                <Menu className="h-4 w-4" />
              </button>
            </div>

            <button
              onClick={onOpenSimulation}
              className="hidden items-center gap-1.5 rounded-xl border border-amber-500/30 bg-amber-500/10 px-3 py-2 text-xs font-semibold text-amber-400 transition-all hover:bg-amber-500/20 xl:flex"
              title="Inject weather and hazard scenarios"
            >
              <Sparkles className="h-4 w-4" />
              <span className="hidden xl:inline">
                Simulate Scenarios
              </span>
              <span className="xl:hidden">
                Simulate
              </span>
            </button>

            <div className="relative group">
              <button
                type="button"
                className="flex h-10 w-10 items-center justify-center rounded-xl border border-slate-600 bg-slate-800/80 text-slate-300 transition hover:border-cyan-400/50 hover:bg-slate-700 hover:text-white"
                title="Choose appearance theme"
                aria-label="Choose appearance theme"
                aria-haspopup="menu"
              >
                {theme === 'light' ? <Sun className="h-4 w-4 text-amber-300" /> : <Moon className="h-4 w-4 text-cyan-300" />}
              </button>
              <div className="theme-menu invisible absolute right-0 top-full z-[120] mt-2 w-56 rounded-xl border border-slate-700 bg-[#0E131F] p-1 opacity-0 shadow-2xl transition group-hover:visible group-hover:opacity-100 group-focus-within:visible group-focus-within:opacity-100" role="menu" aria-label="Appearance themes">
                {themeOptions.map(({ id, label, description, icon: Icon }) => (
                  <button
                    key={id}
                    type="button"
                    onClick={() => onThemeChange?.(id)}
                    className={`theme-menu-item ${theme === id ? 'theme-menu-item-active' : ''}`}
                    role="menuitemradio"
                    aria-checked={theme === id}
                  >
                    <Icon className="theme-menu-icon h-4 w-4 shrink-0" />
                    <span><span className="theme-menu-label block text-xs font-semibold">{label}</span><span className="theme-menu-description block text-[10px]">{description}</span></span>
                  </button>
                ))}
              </div>
            </div>

            <button
              onClick={onOpenEmergencyDemo}
              className="relative flex items-center gap-2 rounded-xl border border-red-400/40 bg-gradient-to-r from-red-600 via-rose-600 to-amber-600 px-3 py-2 text-xs font-bold text-white shadow-lg shadow-red-950/60 transition-all hover:from-red-500 hover:to-amber-500 lg:px-4"
            >
              <span className="relative flex h-2 w-2">
                <span className="absolute inline-flex h-full w-full animate-ping rounded-full bg-white opacity-75" />
                <span className="relative inline-flex h-2 w-2 rounded-full bg-white" />
              </span>

              <PlayCircle className="h-4 w-4" />

              <span className="hidden lg:inline">
                RUN DEMO
              </span>

              <span className="hidden 2xl:inline">
                EMERGENCY
              </span>
            </button>

            {/* NOTIFICATIONS — KEPT SAME */}
            <div className="relative ml-1" ref={notificationRef}>
              <button
                type="button"
                onClick={() =>
                  setIsNotificationsOpen((prev) => !prev)
                }
                className="relative flex h-10 w-10 items-center justify-center rounded-xl border border-slate-600 bg-slate-800/80 text-slate-300 transition hover:border-emerald-400/50 hover:bg-slate-700 hover:text-white"
                title={`${activeAlertsCount} active alerts`}
                aria-label={`${activeAlertsCount} active alerts`}
                aria-expanded={isNotificationsOpen}
                aria-haspopup="dialog"
              >
                <Bell className="h-4 w-4" />

                {activeAlertsCount > 0 && (
                  <span className="absolute -right-1.5 -top-1.5 flex h-5 w-5 items-center justify-center rounded-full border-2 border-[#0E131F] bg-red-500 text-[10px] font-bold text-white">
                    {activeAlertsCount}
                  </span>
                )}
              </button>

              {isNotificationsOpen && (
                <div
                  className="absolute right-0 top-full z-[110] mt-2 w-[min(22rem,calc(100vw-2rem))] overflow-hidden rounded-2xl border border-slate-700 bg-[#0E131F] shadow-2xl shadow-black/50"
                  role="dialog"
                  aria-label="Active notifications"
                >
                  <div className="flex items-center justify-between border-b border-slate-800 px-4 py-3">
                    <div>
                      <p className="text-xs font-bold uppercase tracking-wider text-slate-300">
                        Notifications
                      </p>

                      <p className="text-[11px] text-slate-500">
                        {activeAlertsCount} active notice
                        {activeAlertsCount === 1 ? '' : 's'}
                      </p>
                    </div>

                    <button
                      type="button"
                      onClick={() =>
                        setIsNotificationsOpen(false)
                      }
                      className="rounded-lg p-1.5 text-slate-500 transition hover:bg-slate-800 hover:text-white"
                      aria-label="Close notifications"
                    >
                      <X className="h-4 w-4" />
                    </button>
                  </div>

                  {activeAlerts.length === 0 ? (
                    <div className="px-4 py-8 text-center">
                      <CheckCheck className="mx-auto mb-2 h-7 w-7 text-emerald-400" />

                      <p className="text-xs font-semibold text-slate-300">
                        All corridors clear
                      </p>

                      <p className="mt-1 text-[11px] text-slate-500">
                        No active notifications.
                      </p>
                    </div>
                  ) : (
                    <div className="max-h-80 space-y-2 overflow-y-auto p-2">
                      {activeAlerts.map((alert) => (
                        <div
                          key={alert.id}
                          className="rounded-xl border border-slate-800 bg-slate-900/70 p-3"
                        >
                          <div className="flex items-start gap-2">
                            <AlertTriangle
                              className={`mt-0.5 h-4 w-4 shrink-0 ${alert.severity === 'CRITICAL'
                                ? 'text-red-400'
                                : 'text-amber-400'
                                }`}
                            />

                            <div className="min-w-0 flex-1">
                              <div className="flex items-center justify-between gap-2">
                                <p className="text-[10px] font-mono font-semibold text-amber-300">
                                  {alert.severity || 'NOTICE'}
                                </p>

                                {alert.createdAt && (
                                  <span className="text-[10px] text-slate-500">
                                    {new Date(
                                      alert.createdAt
                                    ).toLocaleTimeString([], {
                                      hour: '2-digit',
                                      minute: '2-digit',
                                    })}
                                  </span>
                                )}
                              </div>

                              <p className="mt-1 text-xs font-semibold text-slate-100">
                                {alert.title}
                              </p>

                              <p className="mt-1 text-[11px] leading-relaxed text-slate-400">
                                {alert.message}
                              </p>

                              {onAcknowledgeAlert &&
                                !alert.acknowledged && (
                                  <button
                                    type="button"
                                    onClick={() =>
                                      onAcknowledgeAlert(alert.id)
                                    }
                                    className="mt-2 text-[10px] font-semibold text-emerald-400 hover:text-emerald-300"
                                  >
                                    Acknowledge
                                  </button>
                                )}
                            </div>
                          </div>
                        </div>
                      ))}
                    </div>
                  )}
                </div>
              )}
            </div>
          </div>
        </div>

        {isNavbarOpen && (
          <div className="absolute left-0 right-0 top-full z-[105] space-y-3 rounded-2xl border border-slate-700/70 bg-slate-900/95 p-3 shadow-2xl lg:hidden">
            <div className="flex min-w-0 items-center gap-2">
              <select
                aria-label="Mobile origin terminal"
                value={origin?.name || ''}
                onChange={handleOriginChange}
                className="min-w-0 flex-1 truncate rounded-xl border border-slate-700 bg-[#141C2B] px-3 py-2.5 text-[11px] text-slate-200 outline-none focus:border-emerald-500"
              >
                {ROUTE_LOCATIONS.map((location) => (
                  <option key={location.name} value={location.name}>
                    {location.name}
                  </option>
                ))}
              </select>

              <span className="shrink-0 text-xs text-slate-500">→</span>

              <select
                aria-label="Mobile destination terminal"
                value={destination?.name || ''}
                onChange={handleDestinationChange}
                className="min-w-0 flex-1 truncate rounded-xl border border-slate-700 bg-[#141C2B] px-3 py-2.5 text-[11px] text-slate-200 outline-none focus:border-cyan-500"
              >
                {ROUTE_LOCATIONS.map((location) => (
                  <option key={location.name} value={location.name}>
                    {location.name}
                  </option>
                ))}
              </select>
            </div>

            <div className="flex items-center justify-between gap-2">
              <div className="flex items-center gap-2 text-[11px] text-slate-400">
                <span className="h-2 w-2 rounded-full bg-emerald-400 shadow-[0_0_8px_rgba(52,211,153,0.8)]" />
                {isAiModel ? 'AI Model Active' : 'Fallback Active'}
              </div>

              <button
                onClick={onOpenSimulation}
                className="flex items-center gap-1.5 rounded-xl border border-amber-500/30 bg-amber-500/10 px-3 py-2 text-xs font-semibold text-amber-400 transition-all hover:bg-amber-500/20"
                title="Inject weather and hazard scenarios"
              >
                <Sparkles className="h-4 w-4" />
                Simulate
              </button>
            </div>
          </div>
        )}
      </div>
    </header>
  );
}