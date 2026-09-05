package com.nerlogistics.backend.dto.route;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RerouteRequest {
    @NotNull(message = "Shipment ID is required")
    private Long shipmentId;

    @NotNull(message = "Selected Route ID is required")
    private Long targetRouteId;

    private String reason;
}
