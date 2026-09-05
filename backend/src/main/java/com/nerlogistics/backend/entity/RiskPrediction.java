package com.nerlogistics.backend.entity;

import com.nerlogistics.backend.enums.RiskLevel;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "risk_predictions", indexes = {
    @Index(name = "idx_risk_lat_lon", columnList = "latitude, longitude")
})
public class RiskPrediction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private Double floodRisk = 0.0;

    @Column(nullable = false)
    private Double landslideRisk = 0.0;

    @Column(nullable = false)
    private Double roadDisruptionRisk = 0.0;

    @Column(nullable = false)
    private Double weatherRisk = 0.0;

    @Column(nullable = false)
    private Double securityRisk = 0.0;

    @Column(nullable = false)
    private Double overallRisk = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RiskLevel riskLevel = RiskLevel.LOW;

    @Column(length = 100)
    private String predictionSource = "RANDOM_FOREST_AI_MODEL";

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime predictedAt;

    public RiskPrediction() {}

    public RiskPrediction(Long id, Double latitude, Double longitude, Double floodRisk, Double landslideRisk, Double roadDisruptionRisk, Double weatherRisk, Double securityRisk, Double overallRisk, RiskLevel riskLevel, String predictionSource, LocalDateTime predictedAt) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.floodRisk = floodRisk != null ? floodRisk : 0.0;
        this.landslideRisk = landslideRisk != null ? landslideRisk : 0.0;
        this.roadDisruptionRisk = roadDisruptionRisk != null ? roadDisruptionRisk : 0.0;
        this.weatherRisk = weatherRisk != null ? weatherRisk : 0.0;
        this.securityRisk = securityRisk != null ? securityRisk : 0.0;
        this.overallRisk = overallRisk != null ? overallRisk : 0.0;
        this.riskLevel = riskLevel != null ? riskLevel : RiskLevel.LOW;
        this.predictionSource = predictionSource != null ? predictionSource : "RANDOM_FOREST_AI_MODEL";
        this.predictedAt = predictedAt;
    }

    public static RiskPredictionBuilder builder() {
        return new RiskPredictionBuilder();
    }

    public static class RiskPredictionBuilder {
        private Long id;
        private Double latitude;
        private Double longitude;
        private Double floodRisk = 0.0;
        private Double landslideRisk = 0.0;
        private Double roadDisruptionRisk = 0.0;
        private Double weatherRisk = 0.0;
        private Double securityRisk = 0.0;
        private Double overallRisk = 0.0;
        private RiskLevel riskLevel = RiskLevel.LOW;
        private String predictionSource = "RANDOM_FOREST_AI_MODEL";
        private LocalDateTime predictedAt;

        public RiskPredictionBuilder id(Long id) { this.id = id; return this; }
        public RiskPredictionBuilder latitude(Double latitude) { this.latitude = latitude; return this; }
        public RiskPredictionBuilder longitude(Double longitude) { this.longitude = longitude; return this; }
        public RiskPredictionBuilder floodRisk(Double floodRisk) { this.floodRisk = floodRisk; return this; }
        public RiskPredictionBuilder landslideRisk(Double landslideRisk) { this.landslideRisk = landslideRisk; return this; }
        public RiskPredictionBuilder roadDisruptionRisk(Double roadDisruptionRisk) { this.roadDisruptionRisk = roadDisruptionRisk; return this; }
        public RiskPredictionBuilder weatherRisk(Double weatherRisk) { this.weatherRisk = weatherRisk; return this; }
        public RiskPredictionBuilder securityRisk(Double securityRisk) { this.securityRisk = securityRisk; return this; }
        public RiskPredictionBuilder overallRisk(Double overallRisk) { this.overallRisk = overallRisk; return this; }
        public RiskPredictionBuilder riskLevel(RiskLevel riskLevel) { this.riskLevel = riskLevel; return this; }
        public RiskPredictionBuilder predictionSource(String predictionSource) { this.predictionSource = predictionSource; return this; }
        public RiskPredictionBuilder predictedAt(LocalDateTime predictedAt) { this.predictedAt = predictedAt; return this; }

        public RiskPrediction build() {
            return new RiskPrediction(id, latitude, longitude, floodRisk, landslideRisk, roadDisruptionRisk, weatherRisk, securityRisk, overallRisk, riskLevel, predictionSource, predictedAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public Double getFloodRisk() { return floodRisk; }
    public void setFloodRisk(Double floodRisk) { this.floodRisk = floodRisk; }
    public Double getLandslideRisk() { return landslideRisk; }
    public void setLandslideRisk(Double landslideRisk) { this.landslideRisk = landslideRisk; }
    public Double getRoadDisruptionRisk() { return roadDisruptionRisk; }
    public void setRoadDisruptionRisk(Double roadDisruptionRisk) { this.roadDisruptionRisk = roadDisruptionRisk; }
    public Double getWeatherRisk() { return weatherRisk; }
    public void setWeatherRisk(Double weatherRisk) { this.weatherRisk = weatherRisk; }
    public Double getSecurityRisk() { return securityRisk; }
    public void setSecurityRisk(Double securityRisk) { this.securityRisk = securityRisk; }
    public Double getOverallRisk() { return overallRisk; }
    public void setOverallRisk(Double overallRisk) { this.overallRisk = overallRisk; }
    public RiskLevel getRiskLevel() { return riskLevel; }
    public void setRiskLevel(RiskLevel riskLevel) { this.riskLevel = riskLevel; }
    public String getPredictionSource() { return predictionSource; }
    public void setPredictionSource(String predictionSource) { this.predictionSource = predictionSource; }
    public LocalDateTime getPredictedAt() { return predictedAt; }
    public void setPredictedAt(LocalDateTime predictedAt) { this.predictedAt = predictedAt; }
}
