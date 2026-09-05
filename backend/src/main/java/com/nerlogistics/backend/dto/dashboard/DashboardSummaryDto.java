package com.nerlogistics.backend.dto.dashboard;

import com.nerlogistics.backend.dto.report.RoadReportResponse;
import com.nerlogistics.backend.dto.shipment.ShipmentResponse;
import com.nerlogistics.backend.dto.vehicle.VehicleResponse;
import com.nerlogistics.backend.entity.Alert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryDto {
    private Long activeVehiclesCount;
    private Long activeShipmentsCount;
    private Long criticalShipmentsCount;
    private Long highRiskRoutesCount;
    private Long blockedRoadsCount;
    private Long criticalAlertsCount;
    private Long totalReportsCount;
    private Long activeAdvisoriesCount;
    private Double averageNetworkRisk; // 0.0 to 100.0 %

    private List<VehicleResponse> activeVehicles;
    private List<ShipmentResponse> criticalShipments;
    private List<RoadReportResponse> recentReports;
    private List<Alert> activeAlerts;
    private Map<String, Integer> shipmentStatusDistribution;
    private Map<String, Integer> vehicleStatusDistribution;
    private Map<String, Integer> riskLevelBreakdown;
}
