package com.nerlogistics.backend.controller;

import com.nerlogistics.backend.dto.dashboard.AnalyticsDto;
import com.nerlogistics.backend.dto.dashboard.DashboardSummaryDto;
import com.nerlogistics.backend.dto.shipment.ShipmentResponse;
import com.nerlogistics.backend.dto.vehicle.VehicleResponse;
import com.nerlogistics.backend.entity.Alert;
import com.nerlogistics.backend.service.AlertService;
import com.nerlogistics.backend.service.DashboardService;
import com.nerlogistics.backend.service.ShipmentService;
import com.nerlogistics.backend.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard & Analytics", description = "KPI metrics, live fleet summaries, risk distributions, and analytics trends")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final VehicleService vehicleService;
    private final ShipmentService shipmentService;
    private final AlertService alertService;

    @GetMapping("/summary")
    @Operation(summary = "Get high-level executive KPI summary and active counts")
    public ResponseEntity<DashboardSummaryDto> getSummary() {
        return ResponseEntity.ok(dashboardService.getSummary());
    }

    @GetMapping("/vehicles")
    @Operation(summary = "Get active vehicle fleet status for dashboard map/table")
    public ResponseEntity<List<VehicleResponse>> getVehicles() {
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }

    @GetMapping("/shipments")
    @Operation(summary = "Get active shipments for dashboard monitoring")
    public ResponseEntity<List<ShipmentResponse>> getShipments() {
        return ResponseEntity.ok(shipmentService.getAllShipments());
    }

    @GetMapping("/alerts")
    @Operation(summary = "Get active alerts feed")
    public ResponseEntity<List<Alert>> getAlerts() {
        return ResponseEntity.ok(alertService.getActiveAlerts());
    }

    @GetMapping("/analytics")
    @Operation(summary = "Get historical analytics trends, corridor reliability, and weather vs delay stats")
    public ResponseEntity<AnalyticsDto> getAnalytics() {
        return ResponseEntity.ok(dashboardService.getAnalytics());
    }
}
