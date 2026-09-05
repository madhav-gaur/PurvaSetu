package com.nerlogistics.backend.entity;

import com.nerlogistics.backend.enums.RoadStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "road_segments")
public class RoadSegment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "road_id", nullable = false)
    private Road road;

    @Column(nullable = false)
    private Double startLatitude;

    @Column(nullable = false)
    private Double startLongitude;

    @Column(nullable = false)
    private Double endLatitude;

    @Column(nullable = false)
    private Double endLongitude;

    @Column(nullable = false)
    private Double length;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RoadStatus status = RoadStatus.OPEN;

    @Column(nullable = false)
    private Double riskScore = 0.0;

    @Column(nullable = false)
    private Double accessibilityScore = 1.0;

    public RoadSegment() {}

    public RoadSegment(Long id, Road road, Double startLatitude, Double startLongitude, Double endLatitude, Double endLongitude, Double length, RoadStatus status, Double riskScore, Double accessibilityScore) {
        this.id = id;
        this.road = road;
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.endLatitude = endLatitude;
        this.endLongitude = endLongitude;
        this.length = length;
        this.status = status != null ? status : RoadStatus.OPEN;
        this.riskScore = riskScore != null ? riskScore : 0.0;
        this.accessibilityScore = accessibilityScore != null ? accessibilityScore : 1.0;
    }

    public static RoadSegmentBuilder builder() {
        return new RoadSegmentBuilder();
    }

    public static class RoadSegmentBuilder {
        private Long id;
        private Road road;
        private Double startLatitude;
        private Double startLongitude;
        private Double endLatitude;
        private Double endLongitude;
        private Double length;
        private RoadStatus status = RoadStatus.OPEN;
        private Double riskScore = 0.0;
        private Double accessibilityScore = 1.0;

        public RoadSegmentBuilder id(Long id) { this.id = id; return this; }
        public RoadSegmentBuilder road(Road road) { this.road = road; return this; }
        public RoadSegmentBuilder startLatitude(Double startLatitude) { this.startLatitude = startLatitude; return this; }
        public RoadSegmentBuilder startLongitude(Double startLongitude) { this.startLongitude = startLongitude; return this; }
        public RoadSegmentBuilder endLatitude(Double endLatitude) { this.endLatitude = endLatitude; return this; }
        public RoadSegmentBuilder endLongitude(Double endLongitude) { this.endLongitude = endLongitude; return this; }
        public RoadSegmentBuilder length(Double length) { this.length = length; return this; }
        public RoadSegmentBuilder status(RoadStatus status) { this.status = status; return this; }
        public RoadSegmentBuilder riskScore(Double riskScore) { this.riskScore = riskScore; return this; }
        public RoadSegmentBuilder accessibilityScore(Double accessibilityScore) { this.accessibilityScore = accessibilityScore; return this; }

        public RoadSegment build() {
            return new RoadSegment(id, road, startLatitude, startLongitude, endLatitude, endLongitude, length, status, riskScore, accessibilityScore);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Road getRoad() { return road; }
    public void setRoad(Road road) { this.road = road; }
    public Double getStartLatitude() { return startLatitude; }
    public void setStartLatitude(Double startLatitude) { this.startLatitude = startLatitude; }
    public Double getStartLongitude() { return startLongitude; }
    public void setStartLongitude(Double startLongitude) { this.startLongitude = startLongitude; }
    public Double getEndLatitude() { return endLatitude; }
    public void setEndLatitude(Double endLatitude) { this.endLatitude = endLatitude; }
    public Double getEndLongitude() { return endLongitude; }
    public void setEndLongitude(Double endLongitude) { this.endLongitude = endLongitude; }
    public Double getLength() { return length; }
    public void setLength(Double length) { this.length = length; }
    public RoadStatus getStatus() { return status; }
    public void setStatus(RoadStatus status) { this.status = status; }
    public Double getRiskScore() { return riskScore; }
    public void setRiskScore(Double riskScore) { this.riskScore = riskScore; }
    public Double getAccessibilityScore() { return accessibilityScore; }
    public void setAccessibilityScore(Double accessibilityScore) { this.accessibilityScore = accessibilityScore; }
}
