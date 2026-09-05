package com.nerlogistics.backend.dto.dashboard;

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
public class AnalyticsDto {
    private List<Map<String, Object>> monthlyDisruptions;
    private List<Map<String, Object>> corridorRiskScores;
    private List<Map<String, Object>> hazardTypeDistribution;
    private List<Map<String, Object>> deliveryOnTimeStats;
    private List<Map<String, Object>> rainfallVsDelayTrends;
    private Double averageRerouteBypassTimeSavedMinutes;
    private Double totalCargoProtectedTons;
}
