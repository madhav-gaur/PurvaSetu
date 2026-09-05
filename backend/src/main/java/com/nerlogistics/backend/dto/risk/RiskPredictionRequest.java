package com.nerlogistics.backend.dto.risk;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiskPredictionRequest {
    private Double latitude;
    private Double longitude;
    private Double rainfall3Hour;
    private Double rainfall1Day;
    private Double humidity;
    private Double windSpeed;
    private Double roadCondition; // 0.0 to 1.0 (1.0 = poor/damaged)
    private Integer recentReports; // count of recent incidents
    private Double floodHistory; // 0.0 to 1.0 vulnerability index
    private Double landslideHistory; // 0.0 to 1.0 slope vulnerability
    private Double securityRisk; // 0.0 to 1.0 advisory weight
    private String shipmentPriority; // "CRITICAL", "HIGH", "MEDIUM", "LOW"
}
