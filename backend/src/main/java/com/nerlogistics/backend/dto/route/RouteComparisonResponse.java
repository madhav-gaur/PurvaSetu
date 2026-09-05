package com.nerlogistics.backend.dto.route;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteComparisonResponse {
    private String origin;
    private String destination;
    private String shipmentPriority;
    private RouteResponseDto recommendedRoute;
    private List<RouteResponseDto> alternativeRoutes;
    private String recommendationReason;
    private Double riskDeltaPercent; // e.g., 58.0% safer
    private Double timeDeltaMinutes; // e.g., +45 mins
}
