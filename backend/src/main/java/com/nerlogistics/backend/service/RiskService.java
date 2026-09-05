package com.nerlogistics.backend.service;

import com.nerlogistics.backend.dto.risk.RiskPredictionRequest;
import com.nerlogistics.backend.dto.risk.RiskPredictionResponse;
import com.nerlogistics.backend.entity.RiskPrediction;
import com.nerlogistics.backend.enums.RiskLevel;
import com.nerlogistics.backend.enums.ShipmentPriority;
import com.nerlogistics.backend.integration.AIServiceClient;
import com.nerlogistics.backend.repository.RiskPredictionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RiskService {

    private final AIServiceClient aiServiceClient;
    private final RiskPredictionRepository riskPredictionRepository;

    /**
     * Calculates or predicts multi-hazard risk for given geographic coordinates and telemetry factors.
     */
    @Transactional
    public RiskPredictionResponse calculateRisk(RiskPredictionRequest request) {
        // 1. Try FastAPI AI Service first
        RiskPredictionResponse aiResponse = aiServiceClient.predictRisk(request);
        if (aiResponse != null) {
            savePrediction(request.getLatitude(), request.getLongitude(), aiResponse);
            return aiResponse;
        }

        // 2. Deterministic Spring Boot Risk Engine Fallback
        return calculateFallbackRisk(request);
    }

    public RiskPredictionResponse calculateFallbackRisk(RiskPredictionRequest req) {
        double r3h = req.getRainfall3Hour() != null ? req.getRainfall3Hour() : 0.0;
        double r1d = req.getRainfall1Day() != null ? req.getRainfall1Day() : 0.0;
        double humidity = req.getHumidity() != null ? req.getHumidity() : 70.0;
        double wind = req.getWindSpeed() != null ? req.getWindSpeed() : 10.0;
        double roadCond = req.getRoadCondition() != null ? req.getRoadCondition() : 0.1;
        int reports = req.getRecentReports() != null ? req.getRecentReports() : 0;
        double floodHist = req.getFloodHistory() != null ? req.getFloodHistory() : 0.2;
        double landslideHist = req.getLandslideHistory() != null ? req.getLandslideHistory() : 0.25;
        double secRisk = req.getSecurityRisk() != null ? req.getSecurityRisk() : 0.05;

        // Flood Risk Formula
        double floodRisk = Math.min(1.0, (r1d / 100.0) * 0.45 + (r3h / 35.0) * 0.35 + floodHist * 0.20);

        // Landslide Risk Formula (heavily influenced by 24h soil saturation and mountain steepness)
        double landslideRisk = Math.min(1.0, (r1d / 80.0) * 0.40 + (r3h / 25.0) * 0.30 + landslideHist * 0.30);

        // Weather Risk
        double weatherRisk = Math.min(1.0, (wind / 45.0) * 0.4 + (r3h / 30.0) * 0.4 + (humidity / 100.0) * 0.2);

        // Disruption Risk (road condition + active field reports + blocked bridges)
        double disruptionRisk = Math.min(1.0, roadCond * 0.4 + Math.min(1.0, reports * 0.2) * 0.4 + (landslideRisk * 0.2));

        // Overall Weighted Risk Baseline:
        // 0.30 * flood + 0.25 * landslide + 0.20 * weather + 0.15 * disruption + 0.10 * security
        double overallRisk = (0.30 * floodRisk) + (0.25 * landslideRisk) + (0.20 * weatherRisk)
                + (0.15 * disruptionRisk) + (0.10 * secRisk);
        overallRisk = Math.min(1.0, Math.max(0.0, overallRisk));

        RiskLevel level;
        if (overallRisk >= 0.81) level = RiskLevel.CRITICAL;
        else if (overallRisk >= 0.61) level = RiskLevel.HIGH;
        else if (overallRisk >= 0.31) level = RiskLevel.MEDIUM;
        else level = RiskLevel.LOW;

        List<String> factors = new ArrayList<>();
        if (floodRisk > 0.5) factors.add("River plain water accumulation & overflow danger (" + Math.round(floodRisk * 100) + "%)");
        if (landslideRisk > 0.5) factors.add("High soil saturation on mountain ghat slope (" + Math.round(landslideRisk * 100) + "%)");
        if (weatherRisk > 0.5) factors.add("Severe monsoon squalls & reduced visibility (" + Math.round(weatherRisk * 100) + "%)");
        if (disruptionRisk > 0.5) factors.add("Active road obstruction or pending field damage reports (" + Math.round(disruptionRisk * 100) + "%)");
        if (secRisk > 0.5) factors.add("Official highway restriction advisory active");

        RiskPredictionResponse response = RiskPredictionResponse.builder()
                .floodRisk(Math.round(floodRisk * 100.0) / 100.0)
                .landslideRisk(Math.round(landslideRisk * 100.0) / 100.0)
                .roadDisruptionRisk(Math.round(disruptionRisk * 100.0) / 100.0)
                .weatherRisk(Math.round(weatherRisk * 100.0) / 100.0)
                .securityRisk(Math.round(secRisk * 100.0) / 100.0)
                .overallRisk(Math.round(overallRisk * 100.0) / 100.0)
                .riskLevel(level)
                .predictionSource("SPRINGBOOT_WEIGHTED_RISK_ENGINE")
                .contributingFactors(factors)
                .recommendation(overallRisk > 0.60 ? "Hazard threshold exceeded! Advise taking safety bypass corridor." : "Route hazard level is manageable.")
                .build();

        if (req.getLatitude() != null && req.getLongitude() != null) {
            savePrediction(req.getLatitude(), req.getLongitude(), response);
        }

        return response;
    }

    /**
     * Computes the dynamic composite cost for route scoring based on shipment priority
     */
    public double calculateRouteScore(double distanceKm, double durationMinutes, double riskScore, ShipmentPriority priority) {
        // Normalize distance (assuming 300km baseline = 1.0) and time (assuming 6h = 360min baseline = 1.0)
        double normDist = distanceKm / 300.0;
        double normTime = durationMinutes / 360.0;

        // Dynamic Priority Weighting:
        // CRITICAL: Risk avoidance is weighted 3.5x higher than distance
        // HIGH: Risk is weighted 2.2x
        // MEDIUM: Balanced
        // LOW: Distance/Time prioritized
        double wDist, wTime, wRisk;
        if (priority == ShipmentPriority.CRITICAL) {
            wDist = 0.15;
            wTime = 0.25;
            wRisk = 0.60;
        } else if (priority == ShipmentPriority.HIGH) {
            wDist = 0.20;
            wTime = 0.35;
            wRisk = 0.45;
        } else if (priority == ShipmentPriority.LOW) {
            wDist = 0.50;
            wTime = 0.35;
            wRisk = 0.15;
        } else {
            wDist = 0.30;
            wTime = 0.40;
            wRisk = 0.30;
        }

        return (wDist * normDist) + (wTime * normTime) + (wRisk * (riskScore * 2.5));
    }

    private void savePrediction(Double lat, Double lon, RiskPredictionResponse res) {
        if (lat == null || lon == null) return;
        try {
            RiskPrediction p = RiskPrediction.builder()
                    .latitude(lat)
                    .longitude(lon)
                    .floodRisk(res.getFloodRisk())
                    .landslideRisk(res.getLandslideRisk())
                    .roadDisruptionRisk(res.getRoadDisruptionRisk())
                    .weatherRisk(res.getWeatherRisk())
                    .securityRisk(res.getSecurityRisk())
                    .overallRisk(res.getOverallRisk())
                    .riskLevel(res.getRiskLevel())
                    .predictionSource(res.getPredictionSource())
                    .predictedAt(LocalDateTime.now())
                    .build();
            riskPredictionRepository.save(p);
        } catch (Exception e) {
            log.warn("Failed to persist risk prediction: {}", e.getMessage());
        }
    }
}
