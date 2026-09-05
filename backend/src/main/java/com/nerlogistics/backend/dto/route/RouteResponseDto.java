package com.nerlogistics.backend.dto.route;

import com.nerlogistics.backend.enums.RiskLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteResponseDto {
    private Long id;
    private String routeName;
    private Double distance; // in km
    private Double duration; // in minutes
    private String formattedDuration; // e.g. "5h 42m"
    private Double riskScore; // 0.0 to 1.0
    private RiskLevel riskLevel; // LOW, MEDIUM, HIGH, CRITICAL
    private Double accessibilityScore; // 0.0 to 1.0
    
    // Risk factor breakdown
    private Double floodRisk;
    private Double landslideRisk;
    private Double weatherRisk;
    private Double roadDisruptionRisk;
    private Double securityRisk;

    private Boolean isRecommended;
    private String safetyRecommendation;
    private String status; // "SAFE_RECOMMENDED", "HIGH_RISK_AVOID", "ALTERNATIVE", "ACTIVE"

    // OSRM GeoJSON geometry: {"type": "LineString", "coordinates": [[lng, lat], ...]}
    private GeoJsonGeometry geometry;

    // Converted coordinates for Leaflet: [[lat, lng], ...]
    private List<List<Double>> leafletCoordinates;
}
