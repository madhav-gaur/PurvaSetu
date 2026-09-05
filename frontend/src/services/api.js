import axios from "axios";

const API_BASE = import.meta.env.VITE_API_BASE_URL || "/api";

const api = axios.create({
  baseURL: API_BASE,
  headers: {
    "Content-Type": "application/json",
  },
  timeout: 10000,
});

export const getDashboardSummary = () =>
  api.get("/dashboard/summary").then((res) => res.data);
export const getAnalytics = () =>
  api.get("/dashboard/analytics").then((res) => res.data);

export const getWeather = (
  lat = 26.1445,
  lon = 91.7362,
  locationName = "Guwahati Hub",
) =>
  api
    .get(
      `/weather/current?latitude=${lat}&longitude=${lon}&locationName=${encodeURIComponent(locationName)}`,
    )
    .then((res) => res.data);

export const getWeatherHistory = () =>
  api.get("/weather/history").then((res) => res.data);

export const getRoutes = (
  startLat,
  startLng,
  destLat,
  destLng,
  priority = "MEDIUM",
) => {
  let url = "/routes";
  if (startLat && startLng && destLat && destLng) {
    url += `?startLat=${startLat}&startLng=${startLng}&destLat=${destLat}&destLng=${destLng}&priority=${priority}`;
  }
  return api.get(url).then((res) => res.data);
};

export const calculateRoutes = (payload) =>
  api.post("/routes/calculate", payload).then((res) => res.data);
export const rerouteShipment = (payload) =>
  api.post("/routes/reroute", payload).then((res) => res.data);

export const getVehicles = () => api.get("/vehicles").then((res) => res.data);
export const getVehicleById = (id) =>
  api.get(`/vehicles/${id}`).then((res) => res.data);
export const createVehicle = (data) =>
  api.post("/vehicles", data).then((res) => res.data);
export const updateVehicleLocation = (id, data) =>
  api.post(`/vehicles/${id}/location`, data).then((res) => res.data);
export const getVehicleLocations = (id) =>
  api.get(`/vehicles/${id}/locations`).then((res) => res.data);

export const getShipments = () => api.get("/shipments").then((res) => res.data);
export const getShipmentById = (id) =>
  api.get(`/shipments/${id}`).then((res) => res.data);
export const createShipment = (data) =>
  api.post("/shipments", data).then((res) => res.data);
export const assignVehicle = (shipmentId, vehicleId) =>
  api
    .post(`/shipments/${shipmentId}/assign-vehicle`, { vehicleId })
    .then((res) => res.data);
export const updateShipmentStatus = (shipmentId, status) =>
  api
    .put(`/shipments/${shipmentId}/status`, { status })
    .then((res) => res.data);

export const getRoadReports = () => api.get("/reports").then((res) => res.data);
export const createRoadReport = (data) =>
  api.post("/reports", data).then((res) => res.data);
export const verifyRoadReport = (id) =>
  api.post(`/reports/${id}/verify`).then((res) => res.data);
export const resolveRoadReport = (id) =>
  api.post(`/reports/${id}/resolve`).then((res) => res.data);

export const getAdvisories = () =>
  api.get("/advisories").then((res) => res.data);
export const createAdvisory = (data) =>
  api.post("/advisories", data).then((res) => res.data);

export const getHazards = () =>
  api.get("/hazards/active").then((res) => res.data);
export const getAllHazards = () => api.get("/hazards").then((res) => res.data);
export const createHazard = (data) =>
  api.post("/hazards", data).then((res) => res.data);

export const getAlerts = () =>
  api.get("/alerts/active").then((res) => res.data);
export const getAllAlerts = () => api.get("/alerts").then((res) => res.data);
export const acknowledgeAlert = (id) =>
  api.put(`/alerts/${id}/acknowledge`).then((res) => res.data);

export const predictRisk = (data) =>
  api.post("/risk/predict", data).then((res) => res.data);

export const triggerSimulation = (scenario) =>
  api.post("/simulation/trigger", { scenario }).then((res) => res.data);

export const runEmergencyDemoStep = (step) =>
  api.get(`/simulation/step/${step}`).then((res) => res.data);

export default api;
