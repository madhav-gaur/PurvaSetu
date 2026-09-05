package com.nerlogistics.backend.dto.simulation;

import com.nerlogistics.backend.dto.route.RouteResponseDto;
import com.nerlogistics.backend.dto.shipment.ShipmentResponse;
import com.nerlogistics.backend.dto.vehicle.VehicleResponse;
import com.nerlogistics.backend.entity.Alert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyDemoStatusDto {
    private Integer currentStep; // 1 to 14
    private Integer totalSteps;
    private String stepTitle;
    private String stepDescription;
    private String stage; // "PLANNING", "IN_TRANSIT", "DISASTER_DETECTED", "REROUTED", "DELIVERED"
    private ShipmentResponse shipment;
    private VehicleResponse vehicle;
    private RouteResponseDto currentRoute;
    private RouteResponseDto alternateRoute;
    private Double currentRiskScore;
    private Alert latestAlert;
    private List<String> logs;
}
