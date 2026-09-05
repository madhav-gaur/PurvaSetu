package com.nerlogistics.backend.entity;

import com.nerlogistics.backend.enums.ReportStatus;
import com.nerlogistics.backend.enums.ReportType;
import com.nerlogistics.backend.enums.Severity;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "road_reports")
public class RoadReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String reporter;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ReportType type;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Severity severity = Severity.MEDIUM;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ReportStatus status = ReportStatus.PENDING;

    @Column(length = 500)
    private String photoUrl;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime verifiedAt;

    public RoadReport() {}

    public RoadReport(Long id, String reporter, Double latitude, Double longitude, ReportType type, String description, Severity severity, ReportStatus status, String photoUrl, LocalDateTime createdAt, LocalDateTime verifiedAt) {
        this.id = id;
        this.reporter = reporter;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.description = description;
        this.severity = severity != null ? severity : Severity.MEDIUM;
        this.status = status != null ? status : ReportStatus.PENDING;
        this.photoUrl = photoUrl;
        this.createdAt = createdAt;
        this.verifiedAt = verifiedAt;
    }

    public static RoadReportBuilder builder() {
        return new RoadReportBuilder();
    }

    public static class RoadReportBuilder {
        private Long id;
        private String reporter;
        private Double latitude;
        private Double longitude;
        private ReportType type;
        private String description;
        private Severity severity = Severity.MEDIUM;
        private ReportStatus status = ReportStatus.PENDING;
        private String photoUrl;
        private LocalDateTime createdAt;
        private LocalDateTime verifiedAt;

        public RoadReportBuilder id(Long id) { this.id = id; return this; }
        public RoadReportBuilder reporter(String reporter) { this.reporter = reporter; return this; }
        public RoadReportBuilder latitude(Double latitude) { this.latitude = latitude; return this; }
        public RoadReportBuilder longitude(Double longitude) { this.longitude = longitude; return this; }
        public RoadReportBuilder type(ReportType type) { this.type = type; return this; }
        public RoadReportBuilder description(String description) { this.description = description; return this; }
        public RoadReportBuilder severity(Severity severity) { this.severity = severity; return this; }
        public RoadReportBuilder status(ReportStatus status) { this.status = status; return this; }
        public RoadReportBuilder photoUrl(String photoUrl) { this.photoUrl = photoUrl; return this; }
        public RoadReportBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public RoadReportBuilder verifiedAt(LocalDateTime verifiedAt) { this.verifiedAt = verifiedAt; return this; }

        public RoadReport build() {
            return new RoadReport(id, reporter, latitude, longitude, type, description, severity, status, photoUrl, createdAt, verifiedAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getReporter() { return reporter; }
    public void setReporter(String reporter) { this.reporter = reporter; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public ReportType getType() { return type; }
    public void setType(ReportType type) { this.type = type; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Severity getSeverity() { return severity; }
    public void setSeverity(Severity severity) { this.severity = severity; }
    public ReportStatus getStatus() { return status; }
    public void setStatus(ReportStatus status) { this.status = status; }
    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getVerifiedAt() { return verifiedAt; }
    public void setVerifiedAt(LocalDateTime verifiedAt) { this.verifiedAt = verifiedAt; }
}
