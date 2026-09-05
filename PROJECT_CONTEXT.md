# NER Smart Logistics Platform: Complete Study Context

## 1. Purpose of This Document

This document is the canonical technical and product context for the NER Smart Logistics project. It is written so that a developer, evaluator, or another AI assistant can understand the project without opening every source file first.

Use it to answer questions about:

- what the platform does and why it exists;
- how the frontend, backend, database, external APIs, and AI service interact;
- where a feature is implemented;
- what each endpoint accepts and returns at a high level;
- how risk scores and route recommendations are produced;
- how to run, test, extend, or troubleshoot the system;
- which parts are production-like and which parts are prototype/demo behavior.

The project was developed for the Smart India Hackathon context. Its domain is resilient logistics and disaster accessibility in India's North Eastern Region (NER): Assam, Meghalaya, Arunachal Pradesh, Nagaland, Manipur, Mizoram, Tripura, and Sikkim.

---

## 2. Product Summary

### Product name

**NER Smart Logistics**

### Product idea

The platform is an operations dashboard for moving critical goods through geographically difficult and disaster-prone corridors. It combines:

1. vehicle and shipment visibility;
2. weather and hazard monitoring;
3. road incident reporting and advisories;
4. route calculation and alternate-route comparison;
5. multi-hazard risk scoring;
6. emergency scenario simulation;
7. an AI risk prediction microservice with a deterministic fallback.

The primary demonstrated use case is emergency medical delivery. A command-center operator can see active vehicles, shipment priorities, weather, road reports, hazards, alerts, risk predictions, and routes in one interface. When a corridor becomes dangerous, the system can recommend a safer alternative and demonstrate a reroute workflow.

### Core value proposition

The platform attempts to answer:

> Given a shipment, its priority, current weather, road condition, reported incidents, and hazard history, which route is operationally safer and how should the logistics team respond?

### Important interpretation

This is a polished SIH prototype rather than a fully deployed national logistics system. It contains real service boundaries, persistence, external API calls, route geometry handling, authentication code, and an ML model, but some data and behavior are synthetic or deterministic for demonstration purposes.

---

## 3. Repository Structure

```text
NER/
├── PROJECT_CONTEXT.md                 # This document
├── .env.example                       # Example environment variables
├── ai-service/                        # FastAPI + scikit-learn risk service
│   ├── app.py                         # API and inference logic
│   ├── README.md                      # AI service setup notes
│   ├── requirements.txt
│   └── model/
│       ├── train.py                   # Synthetic data generation and training
│       └── risk_model.joblib          # Trained Random Forest artifact
├── backend/                           # Spring Boot REST API
│   ├── pom.xml
│   ├── mvnw / mvnw.cmd
│   └── src/main/
│       ├── java/com/nerlogistics/backend/
│       │   ├── NerLogisticsApplication.java
│       │   ├── config/
│       │   ├── controller/
│       │   ├── dto/
│       │   ├── entity/
│       │   ├── enums/
│       │   ├── exception/
│       │   ├── integration/
│       │   ├── repository/
│       │   ├── security/
│       │   └── service/
│       └── resources/application.properties
└── frontend/                          # React + Vite dashboard
    ├── package.json
    ├── index.html
    ├── vite.config.js
    ├── tailwind.config.js
    ├── postcss.config.js
    └── src/
        ├── main.jsx
        ├── App.jsx
        ├── index.css
        ├── components/
        ├── constants/routeLocations.js
        └── services/api.js
```

Generated or local-only folders may also exist, including `backend/target`, `frontend/dist`, and `frontend/node_modules`. They are build outputs/dependencies, not source of truth.

There is also a `backend/bin` directory in the workspace tree. The canonical Maven source is `backend/src`; treat `backend/bin` as a duplicate/generated or auxiliary copy unless a specific build process says otherwise.

---

## 4. Architecture

### Services

```text
Browser
  │
  │ Axios requests to /api
  ▼
React/Vite frontend
  │
  │ Vite dev proxy or same-origin deployment
  ▼
Spring Boot backend :8080
  ├── PostgreSQL database
  ├── OSRM routing API
  ├── Open-Meteo weather API
  └── FastAPI AI service :8000
```

### Responsibilities

#### Frontend

- renders the command-center dashboard;
- owns the selected tab, theme, loaded data, active weather location, and route selection;
- calls the backend through `src/services/api.js`;
- presents maps, charts, tables, alerts, modals, and simulation controls;
- polls the backend every 15 seconds;
- uses Leaflet for the main map and has an optional Mapbox terrain component.

#### Spring Boot backend

- exposes the REST API under `/api`;
- validates request DTOs;
- persists domain records with Spring Data JPA;
- coordinates route, weather, risk, shipment, vehicle, hazard, advisory, alert, and simulation logic;
- calls OSRM, Open-Meteo, and FastAPI;
- seeds a realistic demonstration dataset on an empty database;
- contains JWT authentication infrastructure.

#### FastAPI AI service

- accepts nine numerical risk features and shipment priority;
- loads `risk_model.joblib` at startup;
- predicts five risk outputs with a multi-output Random Forest regressor;
- falls back to analytical formulas if the model is unavailable or inference fails;
- returns risk levels, contributing factors, source, and recommendation.

#### PostgreSQL

- stores users, fleet/telemetry, shipments, roads/segments, routes, reports, advisories, hazards, alerts, weather observations, and risk predictions;
- Hibernate uses `ddl-auto=update`, so tables are updated from entity mappings at startup.

---

## 5. Technology Stack

### Frontend

- React 18.3
- Vite 5
- Tailwind CSS 3
- Axios
- Leaflet and React Leaflet
- Mapbox GL (optional terrain/globe component)
- Recharts
- Lucide React icons
- `clsx` and `tailwind-merge`

### Backend

- Java 17
- Spring Boot 3.2.5
- Spring Web
- Spring Data JPA
- Spring Validation
- Spring Security
- PostgreSQL JDBC driver
- H2 runtime dependency for flexible test/fallback use
- JJWT 0.12.5
- Springdoc OpenAPI 2.5.0
- Jackson Java Time
- Lombok
- Maven Wrapper

### AI service

- Python
- FastAPI
- Uvicorn
- Pydantic
- NumPy
- pandas
- scikit-learn
- joblib

---

## 6. Backend Package Map

Package root: `com.nerlogistics.backend`

### Application/configuration

- `NerLogisticsApplication.java`: Spring Boot entry point.
- `SecurityConfig.java`: stateless security filter chain, BCrypt, JWT filter, endpoint authorization.
- `CorsConfig.java`: CORS configuration.
- `RestClientConfig.java`: shared REST client configuration.
- `DataSeeder.java`: inserts demo users and domain data when tables are empty.

### Controllers

- `AuthController`: register and login.
- `DashboardController`: dashboard summary, lists, and analytics.
- `WeatherController`: weather current/history endpoints.
- `RouteController`: route lookup, calculation, optimization, rerouting, and history.
- `VehicleController`: vehicle CRUD and telemetry.
- `ShipmentController`: shipment CRUD, assignment, and status changes.
- `RoadController`: roads and road segments.
- `RoadReportController`: incident reports and verification/resolution.
- `HazardController`: hazard events.
- `AdvisoryController`: road advisories.
- `AlertController`: active/all alerts and acknowledgement.
- `RiskController`: risk predictions and route risk.
- `SimulationController`: scenario injection and emergency demo steps.
- `UserController`: user CRUD.

### Services

- `UserService`
- `DashboardService`
- `WeatherService`
- `RouteService`
- `RiskService`
- `VehicleService`
- `ShipmentService`
- `RoadService`
- `RoadReportService`
- `HazardService`
- `AdvisoryService`
- `AlertService`
- `SimulationService`
- `GPSService`

Services contain business behavior; controllers mostly translate HTTP requests into service calls.

### Integrations

- `OSRMClient`: calls OSRM and converts route geometry into GeoJSON `[longitude, latitude]` and Leaflet `[latitude, longitude]` formats.
- `WeatherClient`: calls Open-Meteo.
- `AIServiceClient`: calls FastAPI `/predict-risk`.

### Persistence

Repository interfaces are Spring Data `JpaRepository` implementations for the entity types. The main entity classes are listed in Section 8.

### Errors

- `GlobalExceptionHandler`: translates common exceptions into HTTP responses.
- `ResourceNotFoundException`: missing database resource.
- `BadRequestException`: invalid business request.

---

## 7. Frontend Application Map

### Entry points

- `frontend/src/main.jsx`: React DOM entry point.
- `frontend/src/App.jsx`: top-level state, data loading, tab selection, callbacks, and composition.
- `frontend/src/services/api.js`: Axios API wrapper.
- `frontend/src/constants/routeLocations.js`: predefined NER locations and default route.
- `frontend/src/index.css`: global styles and theme overrides.

### Navigation model

There is no React Router. `App.jsx` stores the active view in `currentTab`. `Sidebar.jsx` changes that value, and `App.jsx` conditionally renders one view.

Tabs:

| Tab id | Component | Purpose |
|---|---|---|
| `dashboard` | `DashboardView` | Main overview with summary, weather, route, map/metrics, and alerts |
| `map` | `LeafletMap` | Full interactive map with routes, vehicles, hazards, and reports |
| `routes` | `RouteComparisonView` | Calculate and compare primary/alternate routes |
| `vehicles` | `VehiclesView` | Fleet records and vehicle operations |
| `shipments` | `ShipmentsView` | Shipment records, priorities, assignments, status |
| `reports` | `RoadReportsView` | Road incident creation and review |
| `hazards` | `RiskHazardsView` | Active hazard/risk display |
| `advisories` | `AdvisoriesView` | Road advisory management |
| `analytics` | `AnalyticsView` | Operational analytics and trends |
| `simulation` | `SimulationPanel` | Scenario injection and simulated reports |

### Shared components

- `Navbar.jsx`: centered project identity, global route selector, engine status, simulation button, theme picker, emergency demo button, and notifications.
- `Sidebar.jsx`: tab navigation and count badges.
- `AlertPanel.jsx`: active alert display and acknowledgement.
- `WeatherWidget.jsx`: weather at selectable NER locations.
- `LeafletMap.jsx`: main map with base layers and operational overlays.
- `MapboxTerrainMap.jsx`: optional Mapbox 3D/terrain visualization.
- `EmergencyDemoModal.jsx`: 14-step emergency delivery presentation.
- `SimulationPanel.jsx`: injects predefined weather/hazard scenarios and submits simulated road reports.

### App state and refresh behavior

`App.jsx` owns:

- `currentTab`: selected sidebar view;
- `loading`: initial load state;
- `theme`: persisted in `localStorage` under `ner-theme`;
- `summary`, `analytics`, `weather`, `routes`, `vehicles`, `shipments`, `reports`, `hazards`, `advisories`, `alerts`;
- `routeSelection`: origin and destination objects;
- `isEmergencyDemoOpen` and `isSimulationModalOpen`;
- `isSidebarCollapsed`;
- `weatherLocation`.

At startup and every 15 seconds, `loadAllData()` requests dashboard summary, analytics, weather, vehicles, shipments, reports, hazards, advisories, alerts, and routes in parallel. Each individual call catches failure and supplies an empty fallback, so one failed endpoint does not block the whole dashboard.

Changing the weather location updates state, immediately requests the new weather, and updates the widget.

Changing the global route selector calls `POST /api/routes/calculate` with `priority: CRITICAL`, then replaces the route state.

Acknowledging an alert calls the backend, removes it locally, and reloads all data.

---

## 8. Domain Model

The following entities are persisted with JPA.

### User

Represents a platform account.

Important fields:

- id;
- name;
- email;
- BCrypt password;
- phone;
- role;
- enabled.

Roles include `ADMIN`, `OPERATOR`, `DRIVER`, and `FIELD_AGENT`.

### Vehicle

Represents a truck or logistics vehicle.

Important fields:

- id;
- vehicle number/registration;
- vehicle type;
- driver and driver phone;
- current latitude/longitude;
- speed;
- vehicle status;
- current shipment reference;
- last updated timestamp.

### VehicleLocation

Stores telemetry breadcrumbs separately from the current vehicle record.

Important fields:

- vehicle id;
- latitude;
- longitude;
- speed;
- timestamp.

### Shipment

Represents a consignment.

Important fields:

- id;
- tracking number;
- source;
- destination;
- priority;
- status;
- cargo type;
- weight;
- assigned vehicle;
- expected delivery;
- delivered/updated timestamps.

### Road

Represents a named highway corridor.

Important fields:

- name;
- road number;
- state;
- road status.

### RoadSegment

Represents an operational segment of a road.

Important fields:

- parent road;
- start/end coordinates;
- length;
- status;
- risk score;
- accessibility score.

### Route

Represents a calculated or saved route.

Important fields:

- optional shipment and vehicle relationships;
- origin/destination information;
- distance;
- duration;
- risk score;
- accessibility score;
- serialized GeoJSON geometry;
- route status.

### RouteSegment

Associates a route with a road segment and stores segment-level hazard/risk values.

### RoadReport

Represents an incident reported by an officer, driver, or simulated field agent.

Important fields:

- reporter;
- coordinates;
- report type;
- severity;
- status;
- description;
- photo URL;
- verification timestamp.

### RoadAdvisory

Represents an official or operational advisory tied to a road segment.

Important fields:

- road segment;
- advisory type;
- severity;
- title;
- source;
- validity window;
- status.

### HazardEvent

Represents a hazard such as flood, landslide, heavy rain, or road closure.

Important fields:

- hazard type;
- coordinates;
- severity;
- source;
- lifecycle dates;
- active flag.

### Alert

Represents an actionable notification.

Important fields:

- alert type;
- severity;
- message;
- optional vehicle, shipment, or route reference;
- optional coordinates;
- acknowledgement state;
- timestamps.

### RiskPrediction

Stores a risk assessment.

Important fields:

- flood risk;
- landslide risk;
- road disruption risk;
- weather risk;
- security risk;
- overall risk;
- risk level;
- prediction source;
- timestamp.

### WeatherObservation

Stores Open-Meteo observations used by the platform.

Important fields include temperature, humidity, precipitation, rain, wind, weather code, 3-hour rainfall, and 24-hour rainfall.

### Enums

The domain uses enums including:

- `Role`
- `VehicleStatus`
- `ShipmentStatus`
- `ShipmentPriority`
- `RoadStatus`
- `ReportType`
- `ReportStatus`
- `HazardType`
- `AdvisoryType`
- `AlertType`
- `Severity`
- `RiskLevel`

---

## 9. REST API Reference

Base URL in development: `http://localhost:8080`

All business endpoints use the `/api` prefix. JSON is the normal request/response format.

The exact Java DTO definitions are the authoritative field contract. Swagger is available at `/swagger-ui.html` when the backend is running.

### Authentication

| Method | Endpoint | Purpose |
|---|---|---| Free breakfree
| POST | `/api/auth/register` | Create a user account |
| POST | `/api/auth/login` | Authenticate and receive an auth response/JWT |

### Dashboard

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/dashboard/summary` | Aggregate dashboard counts/summary |
| GET | `/api/dashboard/vehicles` | Dashboard vehicle data |
| GET | `/api/dashboard/shipments` | Dashboard shipment data |
| GET | `/api/dashboard/alerts` | Dashboard alert data |
| GET | `/api/dashboard/analytics` | Analytics DTO and trend/corridor metrics |

### Weather

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/weather` | Weather collection/default weather endpoint |
| GET | `/api/weather/current` | Current weather for latitude, longitude, and location name |
| GET | `/api/weather/history` | Stored weather observations |

Frontend call example:

```text
GET /api/weather/current?latitude=26.1445&longitude=91.7362&locationName=Guwahati%20Logistics%20Hub
```

### Routes

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/routes` | List routes or calculate using query parameters when supplied |
| POST | `/api/routes/calculate` | Calculate and compare routes for an origin/destination |
| POST | `/api/routes/optimize` | Optimize a route request |
| POST | `/api/routes/reroute` | Reroute a shipment/route in response to risk |
| GET | `/api/routes/history` | Route history |
| GET | `/api/routes/{id}` | Retrieve one route |

Typical calculation request shape:

```json
{
  "startLatitude": 26.1445,
  "startLongitude": 91.7362,
  "destinationLatitude": 24.8333,
  "destinationLongitude": 92.7789,
  "originName": "Guwahati Logistics Hub",
  "destinationName": "Silchar Medical Hub",
  "priority": "CRITICAL"
}
```

The response contains origin/destination labels, a recommended route, alternative routes, geometry, distance, duration, risk, accessibility, and prediction-source data used by the UI.

### Vehicles

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/vehicles` | List vehicles |
| GET | `/api/vehicles/{id}` | Retrieve a vehicle |
| POST | `/api/vehicles` | Create a vehicle |
| PUT | `/api/vehicles/{id}` | Update a vehicle |
| DELETE | `/api/vehicles/{id}` | Delete a vehicle |
| POST | `/api/vehicles/{id}/location` | Record/update vehicle location |
| GET | `/api/vehicles/{id}/locations` | Retrieve telemetry history |

### Shipments

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/shipments` | List shipments |
| GET | `/api/shipments/{id}` | Retrieve a shipment |
| POST | `/api/shipments` | Create a shipment |
| PUT | `/api/shipments/{id}` | Update a shipment |
| DELETE | `/api/shipments/{id}` | Delete a shipment |
| POST | `/api/shipments/{id}/assign-vehicle` | Assign a vehicle |
| PUT | `/api/shipments/{id}/status` | Change shipment status |

Assignment request:

```json
{ "vehicleId": 1 }
```

Status request:

```json
{ "status": "IN_TRANSIT" }
```

### Roads

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/roads` | List roads |
| GET | `/api/roads/{id}` | Retrieve a road |
| POST | `/api/roads` | Create a road |
| GET | `/api/roads/segments` | List road segments |
| GET | `/api/roads/{id}/segments` | List segments for one road |

### Road reports

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/reports` | List reports |
| GET | `/api/reports/{id}` | Retrieve a report |
| POST | `/api/reports` | Create a report |
| PUT | `/api/reports/{id}` | Update a report |
| DELETE | `/api/reports/{id}` | Delete a report |
| POST | `/api/reports/{id}/verify` | Verify a report |
| POST | `/api/reports/{id}/resolve` | Resolve a report |

A high-severity report can create an alert through `RoadReportService`.

### Hazards

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/hazards` | List all hazards |
| GET | `/api/hazards/active` | List active hazards |
| POST | `/api/hazards` | Create a hazard |
| PUT | `/api/hazards/{id}/deactivate` | Deactivate a hazard |

### Advisories

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/advisories` | List advisories |
| GET | `/api/advisories/{id}` | Retrieve an advisory |
| POST | `/api/advisories` | Create an advisory |
| PUT | `/api/advisories/{id}` | Update an advisory |
| DELETE | `/api/advisories/{id}` | Delete an advisory |

Creating an advisory can also generate an alert.

### Alerts

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/alerts` | List all alerts |
| GET | `/api/alerts/active` | List active/unacknowledged alerts |
| POST | `/api/alerts` | Create an alert |
| PUT | `/api/alerts/{id}/acknowledge` | Acknowledge an alert |

### Risk

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/risk` | List stored predictions |
| POST | `/api/risk/predict` | Calculate and persist a risk prediction |
| GET | `/api/risk/route/{routeId}` | Retrieve risk for a route |

### Simulation

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/simulation/trigger` | Inject a named scenario |
| GET | `/api/simulation/step/{stepNumber}` | Get/run one emergency demo step |
| POST | `/api/simulation/demo-emergency` | Run or initialize emergency demo behavior |

Implemented scenario names in the backend include `HEAVY_RAIN` and `LANDSLIDE_BLOCKAGE`. The frontend also exposes `FLOOD_SURGE` and `ROAD_CLOSURE`; those frontend options should be checked against `SimulationService` before being treated as supported backend scenarios.

### Users

The controller also exposes user administration endpoints:

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/users` | List users |
| GET | `/api/users/{id}` | Retrieve a user |
| POST | `/api/users` | Create a user |
| PUT | `/api/users/{id}` | Update a user |
| DELETE | `/api/users/{id}` | Delete a user |

### API documentation

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

---

## 10. End-to-End Data Flows

### Initial dashboard load

1. Browser loads React app.
2. `App.jsx` initializes defaults and theme.
3. `loadAllData()` starts parallel Axios calls.
4. Spring controllers delegate to services.
5. Services read PostgreSQL and call external services when needed.
6. Backend returns DTOs.
7. React stores the results and passes them to view components.
8. The app polls the same data every 15 seconds.

### Weather flow

```text
WeatherWidget
  -> getWeather(lat, lon, name)
  -> GET /api/weather/current
  -> WeatherController
  -> WeatherService
  -> WeatherClient
  -> Open-Meteo
  -> save WeatherObservation
  -> WeatherResponseDto
  -> widget/dashboard
```

Weather service logic also derives flood and landslide risk from rainfall-related values.

### Route calculation flow

```text
RouteComparisonView or Navbar selector
  -> POST /api/routes/calculate
  -> RouteController
  -> RouteService.calculateAndCompareRoutes
  -> OSRMClient.fetchRoutes
  -> OSRM route geometry or fallback polyline
  -> RiskService.calculateRisk for each route
  -> AIServiceClient or deterministic fallback
  -> score and rank route alternatives
  -> save Route records
  -> RouteResponseDto
  -> map and route comparison UI
```

### Alert flow

1. A report/advisory/simulation creates a potentially actionable event.
2. The relevant service creates an `Alert`.
3. The navbar and dashboard request active alerts.
4. The operator clicks acknowledgement.
5. Frontend calls `PUT /api/alerts/{id}/acknowledge`.
6. Backend marks it acknowledged.
7. Frontend removes it and refreshes data.

### Emergency demo flow

1. Operator opens the emergency demo modal.
2. The modal advances through 14 steps using `/api/simulation/step/{stepNumber}`.
3. The backend creates or updates the emergency shipment, vehicle, route, hazards, reports, alerts, and delivery state as appropriate for the step.
4. The frontend updates local vehicle/route state and reloads the dashboard.
5. The result is a visual narrative of detection, risk escalation, rerouting, movement, and delivery.

---

## 11. Risk and Route Intelligence

### Risk feature inputs

The AI service expects:

1. `rainfall3Hour`: accumulated 3-hour rainfall in mm;
2. `rainfall1Day`: accumulated 24-hour rainfall in mm;
3. `humidity`: relative humidity percentage;
4. `windSpeed`: wind speed in km/h;
5. `roadCondition`: 0 for good paved road to 1 for severely damaged/unpaved road;
6. `recentReports`: nearby active incident count;
7. `floodHistory`: floodplain vulnerability from 0 to 1;
8. `landslideHistory`: slope/landslide vulnerability from 0 to 1;
9. `securityRisk`: official restriction/curfew weight from 0 to 1;
10. `shipmentPriority`: textual priority, used as domain context.

The current trained model feature vector uses the first nine numerical fields in that order. Priority is accepted in the API contract but is not a numerical model feature in `app.py`.

### AI outputs

The service returns:

- `floodRisk`;
- `landslideRisk`;
- `roadDisruptionRisk`;
- `weatherRisk`;
- `securityRisk`;
- `overallRisk`;
- `riskLevel`;
- `predictionSource`;
- `contributingFactors`;
- `recommendation`.

Each risk is clipped/rounded to the range 0 to 1.

### Risk level thresholds

| Overall score | Level |
|---:|---|
| `< 0.31` | `LOW` |
| `0.31` to `< 0.61` | `MEDIUM` |
| `0.61` to `< 0.81` | `HIGH` |
| `>= 0.81` | `CRITICAL` |

### Deterministic analytical fallback

If FastAPI is offline, the backend can continue using a formula-based fallback. The AI service also has its own fallback. The principal formulas are:

```text
flood = min(1,
  (rainfall1Day / 120) * 0.45 +
  (rainfall3Hour / 35) * 0.30 +
  floodHistory * 0.25)

landslide = min(1,
  (rainfall1Day / 90) * 0.40 +
  (rainfall3Hour / 25) * 0.30 +
  landslideHistory * 0.30)

weather = min(1,
  (windSpeed / 45) * 0.40 +
  (rainfall3Hour / 30) * 0.40 +
  (humidity / 100) * 0.20)

disruption = min(1,
  roadCondition * 0.40 +
  min(1, recentReports / 6) * 0.35 +
  landslide * 0.25)

overall = min(1,
  0.30 * flood +
  0.25 * landslide +
  0.20 * weather +
  0.15 * disruption +
  0.10 * securityRisk)
```

### AI model training

`ai-service/model/train.py`:

- generates 5,000 synthetic samples;
- models the eight NER states through randomized terrain/weather/incident values;
- splits data 80/20;
- trains `RandomForestRegressor(n_estimators=100, max_depth=12, random_state=42, n_jobs=-1)`;
- predicts five targets: flood, landslide, weather, road disruption, and overall risk;
- reports R2 and MSE;
- saves `model/risk_model.joblib`.

The model is therefore domain-grounded synthetic modeling, not a model trained on a verified government or sensor dataset.

### Route selection behavior

`RouteService.calculateAndCompareRoutes`:

1. asks OSRM for alternative driving routes;
2. converts route distance from meters to kilometers and duration from seconds to minutes;
3. if OSRM fails, creates two curved fallback polylines using approximate hill-terrain distance and 42 km/h average speed;
4. evaluates each candidate using route-specific rainfall, terrain, road, and report values;
5. calls the risk service/fallback;
6. applies priority-aware route scoring;
7. saves route records;
8. returns a recommended route plus alternatives.

For `CRITICAL` and `HIGH` shipments, the lower-risk alternative can be selected when the primary route risk exceeds `0.60`.

### Coordinate formats

This is a frequent source of mapping bugs:

- OSRM and GeoJSON use `[longitude, latitude]`;
- Leaflet polyline coordinates use `[latitude, longitude]`;
- `OSRMClient` explicitly creates both representations.

---

## 12. Seeded Demonstration Dataset

`DataSeeder` runs at backend startup and only seeds each category when its repository is empty.

### Demo accounts

| Role | Email | Password |
|---|---|---|
| Admin | `admin@nerlogistics.in` | `Admin@123` |
| Operator | `operator@nerlogistics.in` | `Operator@123` |
| Driver | `driver@nerlogistics.in` | `Driver@123` |
| Field agent | `agent@nerlogistics.in` | `Agent@123` |

These are demonstration credentials, not production credentials.

### Vehicles

The seed includes five regionally plausible vehicles, including:

- `AS-01-GC-4412`, Ashok Leyland cold-chain reefer;
- `AS-01-EC-9081`, Tata 407 heavy truck;
- `ML-05-AB-3341`, Mahindra Bolero Maxi Truck;
- `AR-01-TR-8812`, BharatBenz 4x4 hill hauler;
- `WB-74-J-9901`, Eicher hill truck.

Vehicle positions, driver names, speeds, statuses, and initial shipment references are seeded. Breadcrumb history is seeded for the first vehicle.

### Roads

Seeded corridors include:

- NH-6 Guwahati–Shillong–Silchar corridor;
- NH-27 East-West Highway bypass via Lumding;
- NH-29 Dimapur–Kohima Highway.

Their segments have seeded risk and accessibility scores.

### Shipments

Seeded examples include:

- critical emergency vaccines and life-saving medical rations from Guwahati to Silchar;
- high-priority tea cargo from Jorhat to Guwahati;
- high-priority relief grains and water purification kits to Dibang Valley;
- medium-priority solar/telecom cargo to Agartala;
- high-priority altitude medical kits and heating fuel to Anjaw.

Additional reports, advisories, hazards, and alerts are seeded later in `DataSeeder`.

Because the seeder only checks counts, deleting only some records can leave a mixed dataset; a full database reset is needed to replay a completely fresh seed state.

---

## 13. Configuration and Environment

### Backend configuration

Canonical file: `backend/src/main/resources/application.properties`

Defaults include:

```properties
server.port=8080
spring.application.name=ner-smart-logistics-backend
spring.datasource.url=jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:ner_access_ai}
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD:madhav450}
jwt.secret=${JWT_SECRET:...}
jwt.expiration=${JWT_EXPIRATION_MS:86400000}
osrm.api.url=${OSRM_API_URL:https://router.project-osrm.org/route/v1/driving}
openmeteo.api.url=${OPENMETEO_API_URL:https://api.open-meteo.com/v1/forecast}
ai.service.url=${AI_SERVICE_URL:http://localhost:8000}
```

### Root `.env.example`

The example file documents:

- PostgreSQL URL/user/password;
- JWT secret and expiration;
- OSRM URL;
- Open-Meteo URL;
- AI service URL;
- backend port;
- frontend API base URL.

The current Axios wrapper uses a relative `/api` base URL, so the frontend is expected to use Vite proxying or same-origin deployment. Verify `vite.config.js` when changing deployment topology.

### Security warning

The repository contains development fallback passwords and a development JWT secret in configuration/example material. Replace all secrets, demo credentials, permissive CORS settings, and public endpoint rules before production use. Never reuse the example secret in a deployed environment.

---

## 14. How to Run Locally

### Prerequisites

- Java 17;
- Maven or Maven Wrapper;
- PostgreSQL;
- Python 3 and pip;
- Node.js and npm;
- network access for OSRM and Open-Meteo unless using fallback behavior.

### 1. Create/configure PostgreSQL

Create a database matching the configured name. The canonical property default is `ner_access_ai`; the root example file uses `ner_logistics`, so choose one deliberately and set `DB_NAME` consistently.

Set:

```text
DB_HOST=localhost
DB_PORT=5432
DB_NAME=<database name>
DB_USERNAME=postgres
DB_PASSWORD=<database password>
```

### 2. Start the AI service

```bash
cd ai-service
pip install -r requirements.txt
python model/train.py
uvicorn app:app --host 0.0.0.0 --port 8000 --reload
```

Useful checks:

```text
GET http://localhost:8000/
GET http://localhost:8000/health
POST http://localhost:8000/predict-risk
GET http://localhost:8000/docs
```

The checked-in model artifact normally allows startup without retraining. If it is absent, `app.py` attempts to train it during startup.

### 3. Start the backend

From `backend`:

```bash
mvn spring-boot:run
```

On Windows, use:

```powershell
.mvnw.cmd spring-boot:run
```

The backend listens on port 8080. On first start, Hibernate creates/updates tables and `DataSeeder` inserts the demo dataset where repositories are empty.

### 4. Start the frontend

From `frontend`:

```bash
npm install
npm run dev
```

Build verification:

```bash
npm run build
```

Open the Vite URL shown in the terminal, commonly `http://localhost:5173`.

### 5. Verify service connectivity

Check:

- frontend loads without a permanent loading screen;
- backend API responds at `http://localhost:8080/api/dashboard/summary`;
- Swagger loads at `http://localhost:8080/swagger-ui.html`;
- AI health reports `modelLoaded: true` at `http://localhost:8000/health`;
- PostgreSQL connection succeeds in backend logs;
- browser network calls use the expected `/api` path.

---

## 15. Security and Production Readiness Notes

### Current behavior

`SecurityConfig` enables:

- stateless sessions;
- BCrypt password encoding;
- a JWT authentication filter reading `Authorization: Bearer <token>`;
- a custom user details service;
- method security;
- disabled CSRF for the stateless API.

However, all currently listed feature endpoint groups are configured with `permitAll`, including mutation endpoints such as vehicle creation, shipment updates, reports, hazards, alerts, and users. JWT infrastructure exists, but it does not currently protect those routes.

`CorsConfig` allows all origins with credentials, which is unsafe for production.

### Production hardening checklist

1. Protect mutation and administration endpoints with authentication and role checks.
2. Restrict CORS to the deployed frontend origin(s).
3. Move all secrets to a secure secret manager/environment configuration.
4. Remove or isolate demo credentials and seeded personally identifying contact data.
5. Use HTTPS.
6. Validate rate limits and payload sizes.
7. Add audit logs for reroutes, report verification, alert acknowledgement, and emergency actions.
8. Add database migrations instead of relying only on `ddl-auto=update`.
9. Add real telemetry authentication and replay protection.
10. Replace synthetic risk training data with validated historical/sensor/government data and model monitoring.

---

## 16. Known Prototype Limitations and Caveats

These are important when interpreting results or answering questions about the code.

1. **Synthetic ML data:** the Random Forest is trained on generated data with formula-derived targets. It demonstrates the architecture, not verified predictive accuracy.
2. **Fallback route geometry:** if OSRM is unavailable, the backend produces an approximate curved polyline rather than a road-network route.
3. **Fallback risk calculation:** route evaluation can continue without FastAPI using deterministic formulas.
4. **Analytics are partly demo data:** `DashboardService.getAnalytics()` returns mostly hard-coded trend/corridor values rather than a complete historical analytics pipeline.
5. **Simulation is scripted:** the emergency flow is a 14-step demonstration, not a live event orchestration engine.
6. **Scenario mismatch:** backend `SimulationService` implements `HEAVY_RAIN` and `LANDSLIDE_BLOCKAGE`, while the frontend offers additional scenario labels including `FLOOD_SURGE` and `ROAD_CLOSURE`. Verify support before extending the scenario list.
7. **Authentication is not enforced broadly:** endpoint groups are currently public despite JWT code.
8. **CORS is permissive:** all origins/credentials are allowed by current configuration.
9. **Configuration name mismatch:** `.env.example` and `application.properties` show different default PostgreSQL database names. Set `DB_NAME` explicitly.
10. **No React Router:** navigation is in-memory tab switching, not URL-addressable routes.
11. **Relative frontend API base:** `api.js` uses `/api`; Vite proxy/deployment configuration must provide the backend path.
12. **Polling instead of push:** the dashboard refreshes every 15 seconds; there is no WebSocket/SSE stream.
13. **External API dependency:** weather and route results depend on network availability and third-party service behavior.
14. **Database seed behavior:** seeding is conditional by repository count and is not an idempotent fixture migration.
15. **Generated folders:** `target`, `dist`, and dependency folders should not be edited as source.

---

## 17. Where to Make Common Changes

### Add a new API operation

1. Add/adjust a DTO under `backend/src/main/java/com/nerlogistics/backend/dto`.
2. Add service behavior under `service`.
3. Add a controller mapping under `controller`.
4. Add repository methods only if derived/custom persistence queries are needed.
5. Update `frontend/src/services/api.js`.
6. Update or create the relevant component.
7. Verify through Swagger and frontend build.

### Change route scoring

Inspect `RouteService`, especially:

- `calculateAndCompareRoutes`;
- `calculateRouteScore`;
- the priority and risk threshold logic.

Do not modify only frontend display values if the recommendation itself must change.

### Change risk prediction

Inspect both:

- backend `RiskService` and `AIServiceClient`;
- `ai-service/app.py` and `ai-service/model/train.py`.

Keep feature order synchronized between model training, FastAPI inference, and backend request construction.

### Add a new entity

1. Create the JPA entity.
2. Create its repository.
3. Add DTOs and validation.
4. Add service and controller.
5. Add enum values where appropriate.
6. Add seeding only if demo data is needed.
7. Update frontend API/component state.

### Add a frontend view

1. Create a component under `frontend/src/components`.
2. Add a tab entry in `Sidebar.jsx`.
3. Add conditional rendering and data props in `App.jsx`.
4. Add API functions in `services/api.js` if needed.
5. Preserve existing dark/light/midnight/charcoal theme behavior.

### Change the header

The header is in `frontend/src/components/Navbar.jsx`. It currently uses a centered brand row and a horizontal row beneath it for route selection, engine status, actions, and notifications.

---

## 18. Study Guide: Suggested Reading Order

For a fast but accurate understanding:

1. Read this document's Sections 2, 4, 7, 10, and 11.
2. Read `frontend/src/App.jsx` to understand state and composition.
3. Read `frontend/src/services/api.js` to see the browser/backend contract.
4. Read `backend/src/main/java/com/nerlogistics/backend/controller` to map HTTP endpoints.
5. Read `RouteService.java` and `RiskService.java` for the main intelligence behavior.
6. Read `OSRMClient.java`, `WeatherClient.java`, and `AIServiceClient.java` for integration boundaries.
7. Read `DataSeeder.java` to understand the demo narrative and initial data.
8. Read the entities, DTOs, and enums to understand persistence and validation.
9. Read `ai-service/app.py` and `ai-service/model/train.py` to understand inference and training.
10. Run the three services and use Swagger plus the dashboard while watching logs and browser network requests.

### Questions this project should be able to answer during study

- How does a route become recommended?
- What happens if OSRM fails?
- What happens if the AI service fails?
- Which data is persisted and which data is hard-coded/demo data?
- How does a report create an alert?
- How does alert acknowledgement change the UI?
- What is the difference between GeoJSON and Leaflet coordinate order?
- Why can the dashboard still render when one API is unavailable?
- How would authentication be made real rather than merely implemented?
- How would synthetic training data be replaced with production data?
- How would 15-second polling be replaced by live telemetry?
- Which component owns a given user action?

---

## 19. Compact Explanation for Another AI

You are working on **NER Smart Logistics**, a React/Vite command-center frontend backed by a Java 17 Spring Boot REST API and PostgreSQL, with a FastAPI/scikit-learn risk microservice. The platform manages vehicles, shipments, roads, road reports, advisories, hazards, alerts, weather observations, calculated routes, and risk predictions for disaster-resilient logistics in North East India. The frontend has tab-based navigation without React Router, polls data every 15 seconds, and calls `/api` through Axios. The backend has controllers, services, JPA entities/repositories, DTO validation, seeded demo data, OSRM route integration, Open-Meteo weather integration, and AI-service integration with deterministic fallbacks. Route calculation gets OSRM alternatives or generates fallback polylines, scores them with risk/priority logic, persists routes, and returns GeoJSON plus Leaflet coordinates. Risk outputs are flood, landslide, weather, road disruption, security, and overall values from 0 to 1 with LOW/MEDIUM/HIGH/CRITICAL thresholds. The AI model is a multi-output Random Forest trained on 5,000 synthetic domain-shaped samples. The emergency demo is a scripted 14-step flow. Treat authentication, CORS, development secrets, analytics, and synthetic data as prototype limitations requiring hardening before production. Start services on PostgreSQL, FastAPI port 8000, Spring Boot port 8080, and Vite frontend port 5173; use Swagger at `/swagger-ui.html` and AI docs at `/docs`.

---

## 20. Source-of-Truth Files

When this document and implementation disagree, inspect these files first:

- [backend/src/main/java/com/nerlogistics/backend/service/RouteService.java](backend/src/main/java/com/nerlogistics/backend/service/RouteService.java)
- [backend/src/main/java/com/nerlogistics/backend/service/RiskService.java](backend/src/main/java/com/nerlogistics/backend/service/RiskService.java)
- [backend/src/main/java/com/nerlogistics/backend/service/SimulationService.java](backend/src/main/java/com/nerlogistics/backend/service/SimulationService.java)
- [backend/src/main/java/com/nerlogistics/backend/config/SecurityConfig.java](backend/src/main/java/com/nerlogistics/backend/config/SecurityConfig.java)
- [backend/src/main/java/com/nerlogistics/backend/config/DataSeeder.java](backend/src/main/java/com/nerlogistics/backend/config/DataSeeder.java)
- [frontend/src/App.jsx](frontend/src/App.jsx)
- [frontend/src/services/api.js](frontend/src/services/api.js)
- [ai-service/app.py](ai-service/app.py)
- [ai-service/model/train.py](ai-service/model/train.py)
- [backend/src/main/resources/application.properties](backend/src/main/resources/application.properties)
- [.env.example](.env.example)
