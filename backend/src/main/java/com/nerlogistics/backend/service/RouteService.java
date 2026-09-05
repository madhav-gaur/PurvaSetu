package com.nerlogistics.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nerlogistics.backend.dto.risk.RiskPredictionRequest;
import com.nerlogistics.backend.dto.risk.RiskPredictionResponse;
import com.nerlogistics.backend.dto.route.*;
import com.nerlogistics.backend.entity.Alert;
import com.nerlogistics.backend.entity.Route;
import com.nerlogistics.backend.entity.Shipment;
import com.nerlogistics.backend.entity.Vehicle;
import com.nerlogistics.backend.enums.AlertType;
import com.nerlogistics.backend.enums.RiskLevel;
import com.nerlogistics.backend.enums.Severity;
import com.nerlogistics.backend.enums.ShipmentPriority;
import com.nerlogistics.backend.exception.ResourceNotFoundException;
import com.nerlogistics.backend.integration.OSRMClient;
import com.nerlogistics.backend.repository.AlertRepository;
import com.nerlogistics.backend.repository.RouteRepository;
import com.nerlogistics.backend.repository.ShipmentRepository;
import com.nerlogistics.backend.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RouteService {

    private final OSRMClient osrmClient;
    private final RiskService riskService;
    private final WeatherService weatherService;
    private final RouteRepository routeRepository;
    private final ShipmentRepository shipmentRepository;
    private final VehicleRepository vehicleRepository;
    private final AlertRepository alertRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public RouteComparisonResponse calculateAndCompareRoutes(RouteCalculationRequest request) {
        double sLat = request.getStartLatitude();
        double sLng = request.getStartLongitude();
        double dLat = request.getDestinationLatitude();
        double dLng = request.getDestinationLongitude();
        ShipmentPriority priority = request.getPriority() != null ? request.getPriority() : ShipmentPriority.MEDIUM;

        // 1. Fetch paths from OSRM
        List<OSRMClient.OSRMResult> osrmRoutes = osrmClient.fetchRoutes(sLat, sLng, dLat, dLng);

        Shipment shipment = null;
        if (request.getShipmentId() != null) {
            shipment = shipmentRepository.findById(request.getShipmentId()).orElse(null);
            if (shipment != null) priority = shipment.getPriority();
        }

        Vehicle vehicle = null;
        if (request.getVehicleId() != null) {
            vehicle = vehicleRepository.findById(request.getVehicleId()).orElse(null);
        }

        List<RouteResponseDto> evaluatedRoutes = new ArrayList<>();

        for (int i = 0; i < osrmRoutes.size(); i++) {
            OSRMClient.OSRMResult osrmResult = osrmRoutes.get(i);
            boolean isAlternative = (i > 0);

            // Compute midpoint for weather and hazard check
            double midLat = (sLat + dLat) / 2.0;
            double midLng = (sLng + dLng) / 2.0;
            if (isAlternative) {
                midLat += 0.15;
                midLng += 0.20;
            }

            // In primary route (e.g. NH-6 Meghalaya ghat), simulate realistic active monsoon rainfall hazard
            // In bypass route (e.g. NH-27 Nagaon-Lumding), flatter safer topography
            double r3h = isAlternative ? 4.5 : 28.5;
            double r1d = isAlternative ? 22.0 : 88.0;
            double roadCond = isAlternative ? 0.2 : 0.75;
            int repCount = isAlternative ? 1 : 4;
            double floodHist = isAlternative ? 0.2 : 0.65;
            double landslideHist = isAlternative ? 0.15 : 0.85;

            RiskPredictionRequest riskReq = RiskPredictionRequest.builder()
                    .latitude(midLat)
                    .longitude(midLng)
                    .rainfall3Hour(r3h)
                    .rainfall1Day(r1d)
                    .humidity(isAlternative ? 72.0 : 92.0)
                    .windSpeed(isAlternative ? 12.0 : 26.0)
                    .roadCondition(roadCond)
                    .recentReports(repCount)
                    .floodHistory(floodHist)
                    .landslideHistory(landslideHist)
                    .securityRisk(0.1)
                    .shipmentPriority(priority.name())
                    .build();

            RiskPredictionResponse riskRes = riskService.calculateRisk(riskReq);

            double score = riskService.calculateRouteScore(
                    osrmResult.distanceKm,
                    osrmResult.durationMinutes,
                    riskRes.getOverallRisk(),
                    priority
            );

            String routeName = isAlternative ? "Alternative Route (Northern Disaster Bypass)" : "Primary Highway Corridor";
            long hours = (long) (osrmResult.durationMinutes / 60);
            long mins = (long) (osrmResult.durationMinutes % 60);
            String formattedDur = hours > 0 ? String.format("%dh %02dm", hours, mins) : String.format("%dm", mins);

            // Persist Route Entity
            String geomJson = "";
            try {
                geomJson = objectMapper.writeValueAsString(osrmResult.geoJsonGeometry);
            } catch (Exception ignored) {}

            Route routeEntity = Route.builder()
                    .shipment(shipment)
                    .vehicle(vehicle)
                    .startLatitude(sLat)
                    .startLongitude(sLng)
                    .destinationLatitude(dLat)
                    .destinationLongitude(dLng)
                    .routeName(routeName)
                    .distance(osrmResult.distanceKm)
                    .duration(osrmResult.durationMinutes)
                    .riskScore(riskRes.getOverallRisk())
                    .accessibilityScore(Math.max(0.0, 1.0 - riskRes.getOverallRisk()))
                    .geometryJson(geomJson)
                    .status(isAlternative ? "ALTERNATIVE" : "ACTIVE")
                    .createdAt(LocalDateTime.now())
                    .build();

            routeEntity = routeRepository.save(routeEntity);

            RouteResponseDto dto = RouteResponseDto.builder()
                    .id(routeEntity.getId())
                    .routeName(routeName)
                    .distance(osrmResult.distanceKm)
                    .duration(osrmResult.durationMinutes)
                    .formattedDuration(formattedDur)
                    .riskScore(riskRes.getOverallRisk())
                    .riskLevel(riskRes.getRiskLevel())
                    .accessibilityScore(Math.round((1.0 - riskRes.getOverallRisk()) * 100.0) / 100.0)
                    .floodRisk(riskRes.getFloodRisk())
                    .landslideRisk(riskRes.getLandslideRisk())
                    .weatherRisk(riskRes.getWeatherRisk())
                    .roadDisruptionRisk(riskRes.getRoadDisruptionRisk())
                    .securityRisk(riskRes.getSecurityRisk())
                    .geometry(osrmResult.geoJsonGeometry)
                    .leafletCoordinates(osrmResult.leafletCoordinates)
                    .isRecommended(false)
                    .build();

            evaluatedRoutes.add(dto);
        }

        // Determine best route: For CRITICAL / HIGH shipments, pick lowest risk route
        RouteResponseDto recommended;
        List<RouteResponseDto> alternatives = new ArrayList<>();

        RouteResponseDto routeA = evaluatedRoutes.get(0);
        RouteResponseDto routeB = evaluatedRoutes.size() > 1 ? evaluatedRoutes.get(1) : null;

        if (routeB != null && routeA.getRiskScore() > 0.60 && (priority == ShipmentPriority.CRITICAL || priority == ShipmentPriority.HIGH)) {
            routeB.setIsRecommended(true);
            routeB.setStatus("RECOMMENDED");
            routeB.setSafetyRecommendation(String.format("Recommended because it has %d%% lower hazard probability than the primary corridor.",
                    Math.round((routeA.getRiskScore() - routeB.getRiskScore()) * 100)));
            routeA.setStatus("HIGH_RISK_AVOID");
            routeA.setSafetyRecommendation("High landslide & torrential flooding probability detected on mountain pass.");
            recommended = routeB;
            alternatives.add(routeA);
        } else {
            routeA.setIsRecommended(true);
            routeA.setStatus("RECOMMENDED");
            routeA.setSafetyRecommendation("Optimal balance of safety and travel transit time.");
            recommended = routeA;
            if (routeB != null) {
                routeB.setStatus("ALTERNATIVE");
                alternatives.add(routeB);
            }
        }

        double riskDelta = (routeB != null) ? Math.abs(routeA.getRiskScore() - routeB.getRiskScore()) * 100.0 : 0.0;
        double timeDelta = (routeB != null) ? (routeB.getDuration() - routeA.getDuration()) : 0.0;

        return RouteComparisonResponse.builder()
                .origin(request.getOriginName() != null ? request.getOriginName() : "Guwahati, Assam")
                .destination(request.getDestinationName() != null ? request.getDestinationName() : "Silchar, Assam")
                .shipmentPriority(priority.name())
                .recommendedRoute(recommended)
                .alternativeRoutes(alternatives)
                .recommendationReason(recommended.getSafetyRecommendation())
                .riskDeltaPercent(Math.round(riskDelta * 10.0) / 10.0)
                .timeDeltaMinutes(Math.round(timeDelta * 10.0) / 10.0)
                .build();
    }

    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }

    public Route getRouteById(Long id) {
        return routeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Route not found with id: " + id));
    }

    @Transactional
    public RouteResponseDto rerouteShipment(RerouteRequest request) {
        Shipment shipment = shipmentRepository.findById(request.getShipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found: " + request.getShipmentId()));

        Route targetRoute = routeRepository.findById(request.getTargetRouteId())
                .orElseThrow(() -> new ResourceNotFoundException("Target route not found: " + request.getTargetRouteId()));

        targetRoute.setStatus("ACTIVE");
        targetRoute.setShipment(shipment);
        targetRoute = routeRepository.save(targetRoute);

        // Generate Alert for Reroute
        Alert alert = Alert.builder()
                .type(AlertType.REROUTE)
                .severity(Severity.HIGH)
                .title("Dynamic Reroute Activated for Shipment " + shipment.getTrackingNumber())
                .message("Vehicle diverted to " + targetRoute.getRouteName() + ". Reason: " + (request.getReason() != null ? request.getReason() : "Avoiding high hazard zone on primary corridor."))
                .shipment(shipment)
                .vehicle(shipment.getVehicle())
                .route(targetRoute)
                .acknowledged(false)
                .createdAt(LocalDateTime.now())
                .build();
        alertRepository.save(alert);

        return mapEntityToDto(targetRoute);
    }

    public RouteResponseDto mapEntityToDto(Route route) {
        if (route == null) return null;

        GeoJsonGeometry geo = null;
        List<List<Double>> leafletCoords = new ArrayList<>();
        if (route.getGeometryJson() != null && !route.getGeometryJson().isBlank()) {
            try {
                geo = objectMapper.readValue(route.getGeometryJson(), GeoJsonGeometry.class);
                if (geo != null && geo.getCoordinates() != null) {
                    for (List<Double> pt : geo.getCoordinates()) {
                        leafletCoords.add(List.of(pt.get(1), pt.get(0))); // [lat, lng]
                    }
                }
            } catch (Exception ignored) {}
        }

        long hours = (long) (route.getDuration() / 60);
        long mins = (long) (route.getDuration() % 60);

        return RouteResponseDto.builder()
                .id(route.getId())
                .routeName(route.getRouteName())
                .distance(route.getDistance())
                .duration(route.getDuration())
                .formattedDuration(hours > 0 ? String.format("%dh %02dm", hours, mins) : String.format("%dm", mins))
                .riskScore(route.getRiskScore())
                .riskLevel(route.getRiskScore() > 0.8 ? RiskLevel.CRITICAL : route.getRiskScore() > 0.6 ? RiskLevel.HIGH : route.getRiskScore() > 0.3 ? RiskLevel.MEDIUM : RiskLevel.LOW)
                .accessibilityScore(route.getAccessibilityScore())
                .isRecommended("RECOMMENDED".equalsIgnoreCase(route.getStatus()))
                .safetyRecommendation(route.getSafetyRecommendation())
                .status(route.getStatus())
                .geometry(geo)
                .leafletCoordinates(leafletCoords)
                .build();
    }
}
