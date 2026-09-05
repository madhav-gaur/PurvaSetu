package com.nerlogistics.backend.entity;

import com.nerlogistics.backend.enums.AdvisoryType;
import com.nerlogistics.backend.enums.Severity;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "road_advisories")
public class RoadAdvisory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "road_segment_id")
    private RoadSegment roadSegment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private AdvisoryType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Severity severity = Severity.HIGH;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 100)
    private String source;

    @Column(length = 255)
    private String sourceUrl;

    private LocalDateTime validFrom;
    private LocalDateTime validUntil;

    @Column(length = 30)
    private String status = "ACTIVE";

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public RoadAdvisory() {}

    public RoadAdvisory(Long id, RoadSegment roadSegment, AdvisoryType type, Severity severity, String title, String description, String source, String sourceUrl, LocalDateTime validFrom, LocalDateTime validUntil, String status, LocalDateTime createdAt) {
        this.id = id;
        this.roadSegment = roadSegment;
        this.type = type;
        this.severity = severity != null ? severity : Severity.HIGH;
        this.title = title;
        this.description = description;
        this.source = source;
        this.sourceUrl = sourceUrl;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
        this.status = status != null ? status : "ACTIVE";
        this.createdAt = createdAt;
    }

    public static RoadAdvisoryBuilder builder() {
        return new RoadAdvisoryBuilder();
    }

    public static class RoadAdvisoryBuilder {
        private Long id;
        private RoadSegment roadSegment;
        private AdvisoryType type;
        private Severity severity = Severity.HIGH;
        private String title;
        private String description;
        private String source;
        private String sourceUrl;
        private LocalDateTime validFrom;
        private LocalDateTime validUntil;
        private String status = "ACTIVE";
        private LocalDateTime createdAt;

        public RoadAdvisoryBuilder id(Long id) { this.id = id; return this; }
        public RoadAdvisoryBuilder roadSegment(RoadSegment roadSegment) { this.roadSegment = roadSegment; return this; }
        public RoadAdvisoryBuilder type(AdvisoryType type) { this.type = type; return this; }
        public RoadAdvisoryBuilder severity(Severity severity) { this.severity = severity; return this; }
        public RoadAdvisoryBuilder title(String title) { this.title = title; return this; }
        public RoadAdvisoryBuilder description(String description) { this.description = description; return this; }
        public RoadAdvisoryBuilder source(String source) { this.source = source; return this; }
        public RoadAdvisoryBuilder sourceUrl(String sourceUrl) { this.sourceUrl = sourceUrl; return this; }
        public RoadAdvisoryBuilder validFrom(LocalDateTime validFrom) { this.validFrom = validFrom; return this; }
        public RoadAdvisoryBuilder validUntil(LocalDateTime validUntil) { this.validUntil = validUntil; return this; }
        public RoadAdvisoryBuilder status(String status) { this.status = status; return this; }
        public RoadAdvisoryBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public RoadAdvisory build() {
            return new RoadAdvisory(id, roadSegment, type, severity, title, description, source, sourceUrl, validFrom, validUntil, status, createdAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public RoadSegment getRoadSegment() { return roadSegment; }
    public void setRoadSegment(RoadSegment roadSegment) { this.roadSegment = roadSegment; }
    public AdvisoryType getType() { return type; }
    public void setType(AdvisoryType type) { this.type = type; }
    public Severity getSeverity() { return severity; }
    public void setSeverity(Severity severity) { this.severity = severity; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getSourceUrl() { return sourceUrl; }
    public void setSourceUrl(String sourceUrl) { this.sourceUrl = sourceUrl; }
    public LocalDateTime getValidFrom() { return validFrom; }
    public void setValidFrom(LocalDateTime validFrom) { this.validFrom = validFrom; }
    public LocalDateTime getValidUntil() { return validUntil; }
    public void setValidUntil(LocalDateTime validUntil) { this.validUntil = validUntil; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
