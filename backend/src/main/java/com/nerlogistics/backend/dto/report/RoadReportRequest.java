package com.nerlogistics.backend.dto.report;

import com.nerlogistics.backend.enums.ReportStatus;
import com.nerlogistics.backend.enums.ReportType;
import com.nerlogistics.backend.enums.Severity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RoadReportRequest {
    @NotBlank(message = "Reporter name or ID is required")
    private String reporter;

    @NotNull(message = "Latitude is required")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    private Double longitude;

    @NotNull(message = "Report type is required")
    private ReportType type;

    private String description;
    private Severity severity;
    private ReportStatus status;
    private String photoUrl;

    public RoadReportRequest() {}

    public RoadReportRequest(String reporter, Double latitude, Double longitude, ReportType type, String description, Severity severity, ReportStatus status, String photoUrl) {
        this.reporter = reporter;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.description = description;
        this.severity = severity;
        this.status = status;
        this.photoUrl = photoUrl;
    }

    public static RoadReportRequestBuilder builder() { return new RoadReportRequestBuilder(); }
    public static class RoadReportRequestBuilder {
        private String reporter;
        private Double latitude;
        private Double longitude;
        private ReportType type;
        private String description;
        private Severity severity = Severity.MEDIUM;
        private ReportStatus status = ReportStatus.PENDING;
        private String photoUrl;

        public RoadReportRequestBuilder reporter(String reporter) { this.reporter = reporter; return this; }
        public RoadReportRequestBuilder latitude(Double latitude) { this.latitude = latitude; return this; }
        public RoadReportRequestBuilder longitude(Double longitude) { this.longitude = longitude; return this; }
        public RoadReportRequestBuilder type(ReportType type) { this.type = type; return this; }
        public RoadReportRequestBuilder description(String description) { this.description = description; return this; }
        public RoadReportRequestBuilder severity(Severity severity) { this.severity = severity; return this; }
        public RoadReportRequestBuilder status(ReportStatus status) { this.status = status; return this; }
        public RoadReportRequestBuilder photoUrl(String photoUrl) { this.photoUrl = photoUrl; return this; }

        public RoadReportRequest build() {
            return new RoadReportRequest(reporter, latitude, longitude, type, description, severity, status, photoUrl);
        }
    }

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
}
