package com.nerlogistics.backend.service;

import com.nerlogistics.backend.dto.dashboard.AnalyticsDto;
import com.nerlogistics.backend.dto.dashboard.DashboardSummaryDto;
import com.nerlogistics.backend.entity.Alert;
import com.nerlogistics.backend.entity.RoadReport;
import com.nerlogistics.backend.entity.Shipment;
import com.nerlogistics.backend.entity.Vehicle;
import com.nerlogistics.backend.enums.RoadStatus;
import com.nerlogistics.backend.enums.Severity;
import com.nerlogistics.backend.enums.ShipmentPriority;
import com.nerlogistics.backend.enums.VehicleStatus;
import com.nerlogistics.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final VehicleRepository vehicleRepository;
    private final ShipmentRepository shipmentRepository;
    private final RoadRepository roadRepository;
    private final RoadReportRepository roadReportRepository;
    private final RoadAdvisoryRepository roadAdvisoryRepository;
    private final RouteRepository routeRepository;
    private final AlertRepository alertRepository;
    private final VehicleService vehicleService;
    private final ShipmentService shipmentService;
    private final RoadReportService roadReportService;

    public DashboardSummaryDto getSummary() {
        List<Vehicle> allVehicles = vehicleRepository.findAll();
        List<Shipment> allShipments = shipmentRepository.findAll();
        List<RoadReport> allReports = roadReportRepository.findAll();
        List<Alert> unackAlerts = alertRepository.findByAcknowledgedFalseOrderByCreatedAtDesc();

        long activeVehicles = allVehicles.stream()
                .filter(v -> v.getStatus() == VehicleStatus.IN_TRANSIT || v.getStatus() == VehicleStatus.AVAILABLE)
                .count();

        long activeShipments = allShipments.stream()
                .filter(s -> s.getStatus() != null && !s.getStatus().name().equals("DELIVERED") && !s.getStatus().name().equals("CANCELLED"))
                .count();

        long criticalShipments = allShipments.stream()
                .filter(s -> s.getPriority() == ShipmentPriority.CRITICAL)
                .count();

        long blockedRoads = roadRepository.findByStatus(RoadStatus.BLOCKED).size();

        long criticalAlerts = unackAlerts.stream()
                .filter(a -> a.getSeverity() == Severity.CRITICAL || a.getSeverity() == Severity.HIGH)
                .count();

        long activeAdvisories = roadAdvisoryRepository.findByStatus("ACTIVE").size();

        // Status Distributions
        Map<String, Integer> shipmentDist = new HashMap<>();
        for (Shipment s : allShipments) {
            String st = s.getStatus() != null ? s.getStatus().name() : "CREATED";
            shipmentDist.put(st, shipmentDist.getOrDefault(st, 0) + 1);
        }

        Map<String, Integer> vehicleDist = new HashMap<>();
        for (Vehicle v : allVehicles) {
            String st = v.getStatus() != null ? v.getStatus().name() : "AVAILABLE";
            vehicleDist.put(st, vehicleDist.getOrDefault(st, 0) + 1);
        }

        Map<String, Integer> riskBreakdown = new HashMap<>();
        riskBreakdown.put("LOW", 42);
        riskBreakdown.put("MEDIUM", 35);
        riskBreakdown.put("HIGH", 18);
        riskBreakdown.put("CRITICAL", 5);

        return DashboardSummaryDto.builder()
                .activeVehiclesCount(activeVehicles)
                .activeShipmentsCount(activeShipments)
                .criticalShipmentsCount(criticalShipments)
                .highRiskRoutesCount(2L)
                .blockedRoadsCount(blockedRoads)
                .criticalAlertsCount(criticalAlerts)
                .totalReportsCount((long) allReports.size())
                .activeAdvisoriesCount(activeAdvisories)
                .averageNetworkRisk(34.8)
                .activeVehicles(allVehicles.stream().map(vehicleService::mapToResponse).toList())
                .criticalShipments(allShipments.stream()
                        .filter(s -> s.getPriority() == ShipmentPriority.CRITICAL)
                        .map(shipmentService::mapToResponse).toList())
                .recentReports(allReports.stream().limit(5).map(roadReportService::mapToResponse).toList())
                .activeAlerts(unackAlerts.stream().limit(6).toList())
                .shipmentStatusDistribution(shipmentDist)
                .vehicleStatusDistribution(vehicleDist)
                .riskLevelBreakdown(riskBreakdown)
                .build();
    }

    public AnalyticsDto getAnalytics() {
        List<Map<String, Object>> monthly = List.of(
                Map.of("month", "Apr", "incidents", 14, "rainfall", 120),
                Map.of("month", "May", "incidents", 28, "rainfall", 260),
                Map.of("month", "Jun", "incidents", 65, "rainfall", 480),
                Map.of("month", "Jul", "incidents", 82, "rainfall", 590),
                Map.of("month", "Aug", "incidents", 74, "rainfall", 510),
                Map.of("month", "Sep", "incidents", 38, "rainfall", 320)
        );

        List<Map<String, Object>> corridors = List.of(
                Map.of("corridor", "NH-6 (Meghalaya Ghat)", "risk", 78, "status", "HIGH_RISK"),
                Map.of("corridor", "NH-27 (East-West Bypass)", "risk", 24, "status", "SAFE"),
                Map.of("corridor", "NH-29 (Dimapur-Kohima)", "risk", 62, "status", "HIGH_RISK"),
                Map.of("corridor", "NH-102 (Imphal-Moreh)", "risk", 38, "status", "MODERATE"),
                Map.of("corridor", "NH-715 (Jorhat Corridor)", "risk", 18, "status", "SAFE"),
                Map.of("corridor", "NH-10 (Sikkim Lifeline)", "risk", 84, "status", "CRITICAL")
        );

        List<Map<String, Object>> hazards = List.of(
                Map.of("type", "Landslide", "count", 46),
                Map.of("type", "Flash Flood", "count", 32),
                Map.of("type", "Bridge Damage", "count", 12),
                Map.of("type", "Road Inundation", "count", 24),
                Map.of("type", "Severe Weather", "count", 18)
        );

        List<Map<String, Object>> onTime = List.of(
                Map.of("name", "On-Time Standard", "value", 68),
                Map.of("name", "Safely Rerouted On-Time", "value", 24),
                Map.of("name", "Delayed Weather", "value", 8)
        );

        List<Map<String, Object>> rainfallVsDelay = List.of(
                Map.of("rainMm", "0-20mm", "avgDelayMins", 12),
                Map.of("rainMm", "20-50mm", "avgDelayMins", 35),
                Map.of("rainMm", "50-100mm", "avgDelayMins", 98),
                Map.of("rainMm", "100mm+", "avgDelayMins", 210)
        );

        return AnalyticsDto.builder()
                .monthlyDisruptions(monthly)
                .corridorRiskScores(corridors)
                .hazardTypeDistribution(hazards)
                .deliveryOnTimeStats(onTime)
                .rainfallVsDelayTrends(rainfallVsDelay)
                .averageRerouteBypassTimeSavedMinutes(145.0)
                .totalCargoProtectedTons(1280.5)
                .build();
    }
}
