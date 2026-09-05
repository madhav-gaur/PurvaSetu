package com.nerlogistics.backend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "routes")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shipment_id")
    private Shipment shipment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @Column(nullable = false)
    private Double startLatitude;

    @Column(nullable = false)
    private Double startLongitude;

    @Column(nullable = false)
    private Double destinationLatitude;

    @Column(nullable = false)
    private Double destinationLongitude;

    @Column(length = 150)
    private String routeName;

    @Column(nullable = false)
    private Double distance;

    @Column(nullable = false)
    private Double duration;

    @Column(nullable = false)
    private Double riskScore = 0.0;

    @Column(nullable = false)
    private Double accessibilityScore = 1.0;

    @Column(columnDefinition = "TEXT")
    private String geometryJson;

    @Column(columnDefinition = "TEXT")
    private String safetyRecommendation;

    @Column(length = 30)
    private String status = "ACTIVE";

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Route() {}

    public Route(Long id, Shipment shipment, Vehicle vehicle, Double startLatitude, Double startLongitude, Double destinationLatitude, Double destinationLongitude, String routeName, Double distance, Double duration, Double riskScore, Double accessibilityScore, String geometryJson, String safetyRecommendation, String status, LocalDateTime createdAt) {
        this.id = id;
        this.shipment = shipment;
        this.vehicle = vehicle;
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.destinationLatitude = destinationLatitude;
        this.destinationLongitude = destinationLongitude;
        this.routeName = routeName;
        this.distance = distance;
        this.duration = duration;
        this.riskScore = riskScore != null ? riskScore : 0.0;
        this.accessibilityScore = accessibilityScore != null ? accessibilityScore : 1.0;
        this.geometryJson = geometryJson;
        this.safetyRecommendation = safetyRecommendation;
        this.status = status != null ? status : "ACTIVE";
        this.createdAt = createdAt;
    }

    public static RouteBuilder builder() {
        return new RouteBuilder();
    }

    public static class RouteBuilder {
        private Long id;
        private Shipment shipment;
        private Vehicle vehicle;
        private Double startLatitude;
        private Double startLongitude;
        private Double destinationLatitude;
        private Double destinationLongitude;
        private String routeName;
        private Double distance;
        private Double duration;
        private Double riskScore = 0.0;
        private Double accessibilityScore = 1.0;
        private String geometryJson;
        private String safetyRecommendation;
        private String status = "ACTIVE";
        private LocalDateTime createdAt;

        public RouteBuilder id(Long id) { this.id = id; return this; }
        public RouteBuilder shipment(Shipment shipment) { this.shipment = shipment; return this; }
        public RouteBuilder vehicle(Vehicle vehicle) { this.vehicle = vehicle; return this; }
        public RouteBuilder startLatitude(Double startLatitude) { this.startLatitude = startLatitude; return this; }
        public RouteBuilder startLongitude(Double startLongitude) { this.startLongitude = startLongitude; return this; }
        public RouteBuilder destinationLatitude(Double destinationLatitude) { this.destinationLatitude = destinationLatitude; return this; }
        public RouteBuilder destinationLongitude(Double destinationLongitude) { this.destinationLongitude = destinationLongitude; return this; }
        public RouteBuilder routeName(String routeName) { this.routeName = routeName; return this; }
        public RouteBuilder distance(Double distance) { this.distance = distance; return this; }
        public RouteBuilder duration(Double duration) { this.duration = duration; return this; }
        public RouteBuilder riskScore(Double riskScore) { this.riskScore = riskScore; return this; }
        public RouteBuilder accessibilityScore(Double accessibilityScore) { this.accessibilityScore = accessibilityScore; return this; }
        public RouteBuilder geometryJson(String geometryJson) { this.geometryJson = geometryJson; return this; }
        public RouteBuilder safetyRecommendation(String safetyRecommendation) { this.safetyRecommendation = safetyRecommendation; return this; }
        public RouteBuilder status(String status) { this.status = status; return this; }
        public RouteBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public Route build() {
            return new Route(id, shipment, vehicle, startLatitude, startLongitude, destinationLatitude, destinationLongitude, routeName, distance, duration, riskScore, accessibilityScore, geometryJson, safetyRecommendation, status, createdAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Shipment getShipment() { return shipment; }
    public void setShipment(Shipment shipment) { this.shipment = shipment; }
    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }
    public Double getStartLatitude() { return startLatitude; }
    public void setStartLatitude(Double startLatitude) { this.startLatitude = startLatitude; }
    public Double getStartLongitude() { return startLongitude; }
    public void setStartLongitude(Double startLongitude) { this.startLongitude = startLongitude; }
    public Double getDestinationLatitude() { return destinationLatitude; }
    public void setDestinationLatitude(Double destinationLatitude) { this.destinationLatitude = destinationLatitude; }
    public Double getDestinationLongitude() { return destinationLongitude; }
    public void setDestinationLongitude(Double destinationLongitude) { this.destinationLongitude = destinationLongitude; }
    public String getRouteName() { return routeName; }
    public void setRouteName(String routeName) { this.routeName = routeName; }
    public Double getDistance() { return distance; }
    public void setDistance(Double distance) { this.distance = distance; }
    public Double getDuration() { return duration; }
    public void setDuration(Double duration) { this.duration = duration; }
    public Double getRiskScore() { return riskScore; }
    public void setRiskScore(Double riskScore) { this.riskScore = riskScore; }
    public Double getAccessibilityScore() { return accessibilityScore; }
    public void setAccessibilityScore(Double accessibilityScore) { this.accessibilityScore = accessibilityScore; }
    public String getGeometryJson() { return geometryJson; }
    public void setGeometryJson(String geometryJson) { this.geometryJson = geometryJson; }
    public String getSafetyRecommendation() { return safetyRecommendation; }
    public void setSafetyRecommendation(String safetyRecommendation) { this.safetyRecommendation = safetyRecommendation; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
