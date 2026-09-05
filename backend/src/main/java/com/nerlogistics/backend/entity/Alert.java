package com.nerlogistics.backend.entity;

import com.nerlogistics.backend.enums.AlertType;
import com.nerlogistics.backend.enums.Severity;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "alerts")
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private AlertType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Severity severity = Severity.HIGH;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String message;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shipment_id")
    private Shipment shipment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "route_id")
    private Route route;

    private Double latitude;
    private Double longitude;

    @Column(nullable = false)
    private Boolean acknowledged = false;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Alert() {}

    public Alert(Long id, AlertType type, Severity severity, String title, String message, Vehicle vehicle, Shipment shipment, Route route, Double latitude, Double longitude, Boolean acknowledged, LocalDateTime createdAt) {
        this.id = id;
        this.type = type;
        this.severity = severity != null ? severity : Severity.HIGH;
        this.title = title;
        this.message = message;
        this.vehicle = vehicle;
        this.shipment = shipment;
        this.route = route;
        this.latitude = latitude;
        this.longitude = longitude;
        this.acknowledged = acknowledged != null ? acknowledged : false;
        this.createdAt = createdAt;
    }

    public static AlertBuilder builder() {
        return new AlertBuilder();
    }

    public static class AlertBuilder {
        private Long id;
        private AlertType type;
        private Severity severity = Severity.HIGH;
        private String title;
        private String message;
        private Vehicle vehicle;
        private Shipment shipment;
        private Route route;
        private Double latitude;
        private Double longitude;
        private Boolean acknowledged = false;
        private LocalDateTime createdAt;

        public AlertBuilder id(Long id) { this.id = id; return this; }
        public AlertBuilder type(AlertType type) { this.type = type; return this; }
        public AlertBuilder severity(Severity severity) { this.severity = severity; return this; }
        public AlertBuilder title(String title) { this.title = title; return this; }
        public AlertBuilder message(String message) { this.message = message; return this; }
        public AlertBuilder vehicle(Vehicle vehicle) { this.vehicle = vehicle; return this; }
        public AlertBuilder shipment(Shipment shipment) { this.shipment = shipment; return this; }
        public AlertBuilder route(Route route) { this.route = route; return this; }
        public AlertBuilder latitude(Double latitude) { this.latitude = latitude; return this; }
        public AlertBuilder longitude(Double longitude) { this.longitude = longitude; return this; }
        public AlertBuilder acknowledged(Boolean acknowledged) { this.acknowledged = acknowledged; return this; }
        public AlertBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public Alert build() {
            return new Alert(id, type, severity, title, message, vehicle, shipment, route, latitude, longitude, acknowledged, createdAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public AlertType getType() { return type; }
    public void setType(AlertType type) { this.type = type; }
    public Severity getSeverity() { return severity; }
    public void setSeverity(Severity severity) { this.severity = severity; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }
    public Shipment getShipment() { return shipment; }
    public void setShipment(Shipment shipment) { this.shipment = shipment; }
    public Route getRoute() { return route; }
    public void setRoute(Route route) { this.route = route; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public Boolean getAcknowledged() { return acknowledged; }
    public void setAcknowledged(Boolean acknowledged) { this.acknowledged = acknowledged; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
