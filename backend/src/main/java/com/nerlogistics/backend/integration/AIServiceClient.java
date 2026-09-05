package com.nerlogistics.backend.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nerlogistics.backend.dto.risk.RiskPredictionRequest;
import com.nerlogistics.backend.dto.risk.RiskPredictionResponse;
import com.nerlogistics.backend.enums.RiskLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class AIServiceClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${ai.service.url:http://localhost:8000}")
    private String aiServiceUrl;

    public RiskPredictionResponse predictRisk(RiskPredictionRequest request) {
        String url = aiServiceUrl + "/predict-risk";
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<RiskPredictionRequest> entity = new HttpEntity<>(request, headers);

            String responseStr = restTemplate.postForObject(url, entity, String.class);
            if (responseStr != null) {
                JsonNode root = objectMapper.readTree(responseStr);
                double floodRisk = root.path("floodRisk").asDouble(0.0);
                double landslideRisk = root.path("landslideRisk").asDouble(0.0);
                double disruptionRisk = root.path("roadDisruptionRisk").asDouble(0.0);
                double weatherRisk = root.path("weatherRisk").asDouble(0.0);
                double securityRisk = root.path("securityRisk").asDouble(0.0);
                double overallRisk = root.path("overallRisk").asDouble(0.0);
                String riskLevelStr = root.path("riskLevel").asText("LOW");

                RiskLevel level;
                try {
                    level = RiskLevel.valueOf(riskLevelStr.toUpperCase());
                } catch (Exception e) {
                    level = overallRisk > 0.8 ? RiskLevel.CRITICAL : overallRisk > 0.6 ? RiskLevel.HIGH : overallRisk > 0.3 ? RiskLevel.MEDIUM : RiskLevel.LOW;
                }

                List<String> factors = new ArrayList<>();
                if (floodRisk > 0.5) factors.add("Elevated flood inundation probability");
                if (landslideRisk > 0.5) factors.add("Steep slope rain saturation triggering landslide risk");
                if (weatherRisk > 0.5) factors.add("Severe precipitation and gale winds");
                if (disruptionRisk > 0.5) factors.add("Active road blockage or damaged infrastructure");
                if (securityRisk > 0.5) factors.add("Official highway restriction or curfew advisory");

                return RiskPredictionResponse.builder()
                        .floodRisk(Math.round(floodRisk * 100.0) / 100.0)
                        .landslideRisk(Math.round(landslideRisk * 100.0) / 100.0)
                        .roadDisruptionRisk(Math.round(disruptionRisk * 100.0) / 100.0)
                        .weatherRisk(Math.round(weatherRisk * 100.0) / 100.0)
                        .securityRisk(Math.round(securityRisk * 100.0) / 100.0)
                        .overallRisk(Math.round(overallRisk * 100.0) / 100.0)
                        .riskLevel(level)
                        .predictionSource("FASTAPI_RANDOM_FOREST_ML")
                        .contributingFactors(factors)
                        .recommendation(overallRisk > 0.6 ? "High hazard risk detected. Reroute via safe disaster bypass recommended." : "Route is within acceptable safety parameters.")
                        .build();
            }
        } catch (Exception e) {
            log.info("AI Service at {} not reachable: {}. Using Spring Boot Risk Engine fallback.", url, e.getMessage());
        }
        return null;
    }
}
