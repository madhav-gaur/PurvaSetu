package com.nerlogistics.backend.dto.risk;

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
public class RiskPredictionResponse {
    private Double floodRisk;
    private Double landslideRisk;
    private Double roadDisruptionRisk;
    private Double weatherRisk;
    private Double securityRisk;
    private Double overallRisk;
    private RiskLevel riskLevel; // LOW, MEDIUM, HIGH, CRITICAL
    private String predictionSource; // e.g. "FASTAPI_RANDOM_FOREST" or "SPRINGBOOT_WEIGHTED_ENGINE"
    private List<String> contributingFactors;
    private String recommendation;
}
