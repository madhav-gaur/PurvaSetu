package com.nerlogistics.backend.dto.route;

import com.nerlogistics.backend.enums.ShipmentPriority;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteCalculationRequest {
    @NotNull(message = "Start latitude is required")
    private Double startLatitude;

    @NotNull(message = "Start longitude is required")
    private Double startLongitude;

    @NotNull(message = "Destination latitude is required")
    private Double destinationLatitude;

    @NotNull(message = "Destination longitude is required")
    private Double destinationLongitude;

    private Long shipmentId;
    private Long vehicleId;
    
    @Builder.Default
    private ShipmentPriority priority = ShipmentPriority.MEDIUM;
    
    private String originName;
    private String destinationName;
}
