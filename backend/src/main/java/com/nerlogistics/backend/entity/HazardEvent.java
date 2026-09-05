package com.nerlogistics.backend.entity;

import com.nerlogistics.backend.enums.HazardType;
import com.nerlogistics.backend.enums.Severity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "hazard_events")
public class HazardEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private HazardType type;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Severity severity = Severity.HIGH;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 100)
    private String source;

    @Column(length = 255)
    private String sourceUrl;

    private LocalDateTime occurredAt;
    private LocalDateTime expiresAt;

    @Column(length = 30)
    private String status = "ACTIVE";

    public HazardEvent() {}

    public HazardEvent(Long id, HazardType type, Double latitude, Double longitude, Severity severity, String description, String source, String sourceUrl, LocalDateTime occurredAt, LocalDateTime expiresAt, String status) {
        this.id = id;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.severity = severity != null ? severity : Severity.HIGH;
        this.description = description;
        this.source = source;
        this.sourceUrl = sourceUrl;
        this.occurredAt = occurredAt;
        this.expiresAt = expiresAt;
        this.status = status != null ? status : "ACTIVE";
    }

    public static HazardEventBuilder builder() {
        return new HazardEventBuilder();
    }

    public static class HazardEventBuilder {
        private Long id;
        private HazardType type;
        private Double latitude;
        private Double longitude;
        private Severity severity = Severity.HIGH;
        private String description;
        private String source;
        private String sourceUrl;
        private LocalDateTime occurredAt;
        private LocalDateTime expiresAt;
        private String status = "ACTIVE";

        public HazardEventBuilder id(Long id) { this.id = id; return this; }
        public HazardEventBuilder type(HazardType type) { this.type = type; return this; }
        public HazardEventBuilder latitude(Double latitude) { this.latitude = latitude; return this; }
        public HazardEventBuilder longitude(Double longitude) { this.longitude = longitude; return this; }
        public HazardEventBuilder severity(Severity severity) { this.severity = severity; return this; }
        public HazardEventBuilder description(String description) { this.description = description; return this; }
        public HazardEventBuilder source(String source) { this.source = source; return this; }
        public HazardEventBuilder sourceUrl(String sourceUrl) { this.sourceUrl = sourceUrl; return this; }
        public HazardEventBuilder occurredAt(LocalDateTime occurredAt) { this.occurredAt = occurredAt; return this; }
        public HazardEventBuilder expiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; return this; }
        public HazardEventBuilder status(String status) { this.status = status; return this; }

        public HazardEvent build() {
            return new HazardEvent(id, type, latitude, longitude, severity, description, source, sourceUrl, occurredAt, expiresAt, status);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public HazardType getType() { return type; }
    public void setType(HazardType type) { this.type = type; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public Severity getSeverity() { return severity; }
    public void setSeverity(Severity severity) { this.severity = severity; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getSourceUrl() { return sourceUrl; }
    public void setSourceUrl(String sourceUrl) { this.sourceUrl = sourceUrl; }
    public LocalDateTime getOccurredAt() { return occurredAt; }
    public void setOccurredAt(LocalDateTime occurredAt) { this.occurredAt = occurredAt; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
