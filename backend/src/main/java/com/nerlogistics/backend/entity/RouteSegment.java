package com.nerlogistics.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "route_segments")
public class RouteSegment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "road_segment_id")
    private RoadSegment roadSegment;

    @Column(nullable = false)
    private Double distance;

    @Column(nullable = false)
    private Double duration;

    @Column(nullable = false)
    private Double floodRisk = 0.0;

    @Column(nullable = false)
    private Double landslideRisk = 0.0;

    @Column(nullable = false)
    private Double disruptionRisk = 0.0;

    @Column(nullable = false)
    private Double weatherRisk = 0.0;

    @Column(nullable = false)
    private Double securityRisk = 0.0;

    @Column(nullable = false)
    private Double accessibilityScore = 1.0;

    public RouteSegment() {}

    public RouteSegment(Long id, Route route, RoadSegment roadSegment, Double distance, Double duration, Double floodRisk, Double landslideRisk, Double disruptionRisk, Double weatherRisk, Double securityRisk, Double accessibilityScore) {
        this.id = id;
        this.route = route;
        this.roadSegment = roadSegment;
        this.distance = distance;
        this.duration = duration;
        this.floodRisk = floodRisk != null ? floodRisk : 0.0;
        this.landslideRisk = landslideRisk != null ? landslideRisk : 0.0;
        this.disruptionRisk = disruptionRisk != null ? disruptionRisk : 0.0;
        this.weatherRisk = weatherRisk != null ? weatherRisk : 0.0;
        this.securityRisk = securityRisk != null ? securityRisk : 0.0;
        this.accessibilityScore = accessibilityScore != null ? accessibilityScore : 1.0;
    }

    public static RouteSegmentBuilder builder() {
        return new RouteSegmentBuilder();
    }

    public static class RouteSegmentBuilder {
        private Long id;
        private Route route;
        private RoadSegment roadSegment;
        private Double distance;
        private Double duration;
        private Double floodRisk = 0.0;
        private Double landslideRisk = 0.0;
        private Double disruptionRisk = 0.0;
        private Double weatherRisk = 0.0;
        private Double securityRisk = 0.0;
        private Double accessibilityScore = 1.0;

        public RouteSegmentBuilder id(Long id) { this.id = id; return this; }
        public RouteSegmentBuilder route(Route route) { this.route = route; return this; }
        public RouteSegmentBuilder roadSegment(RoadSegment roadSegment) { this.roadSegment = roadSegment; return this; }
        public RouteSegmentBuilder distance(Double distance) { this.distance = distance; return this; }
        public RouteSegmentBuilder duration(Double duration) { this.duration = duration; return this; }
        public RouteSegmentBuilder floodRisk(Double floodRisk) { this.floodRisk = floodRisk; return this; }
        public RouteSegmentBuilder landslideRisk(Double landslideRisk) { this.landslideRisk = landslideRisk; return this; }
        public RouteSegmentBuilder disruptionRisk(Double disruptionRisk) { this.disruptionRisk = disruptionRisk; return this; }
        public RouteSegmentBuilder weatherRisk(Double weatherRisk) { this.weatherRisk = weatherRisk; return this; }
        public RouteSegmentBuilder securityRisk(Double securityRisk) { this.securityRisk = securityRisk; return this; }
        public RouteSegmentBuilder accessibilityScore(Double accessibilityScore) { this.accessibilityScore = accessibilityScore; return this; }

        public RouteSegment build() {
            return new RouteSegment(id, route, roadSegment, distance, duration, floodRisk, landslideRisk, disruptionRisk, weatherRisk, securityRisk, accessibilityScore);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Route getRoute() { return route; }
    public void setRoute(Route route) { this.route = route; }
    public RoadSegment getRoadSegment() { return roadSegment; }
    public void setRoadSegment(RoadSegment roadSegment) { this.roadSegment = roadSegment; }
    public Double getDistance() { return distance; }
    public void setDistance(Double distance) { this.distance = distance; }
    public Double getDuration() { return duration; }
    public void setDuration(Double duration) { this.duration = duration; }
    public Double getFloodRisk() { return floodRisk; }
    public void setFloodRisk(Double floodRisk) { this.floodRisk = floodRisk; }
    public Double getLandslideRisk() { return landslideRisk; }
    public void setLandslideRisk(Double landslideRisk) { this.landslideRisk = landslideRisk; }
    public Double getDisruptionRisk() { return disruptionRisk; }
    public void setDisruptionRisk(Double disruptionRisk) { this.disruptionRisk = disruptionRisk; }
    public Double getWeatherRisk() { return weatherRisk; }
    public void setWeatherRisk(Double weatherRisk) { this.weatherRisk = weatherRisk; }
    public Double getSecurityRisk() { return securityRisk; }
    public void setSecurityRisk(Double securityRisk) { this.securityRisk = securityRisk; }
    public Double getAccessibilityScore() { return accessibilityScore; }
    public void setAccessibilityScore(Double accessibilityScore) { this.accessibilityScore = accessibilityScore; }
}
