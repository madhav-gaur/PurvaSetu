package com.nerlogistics.backend.entity;

import com.nerlogistics.backend.enums.ShipmentPriority;
import com.nerlogistics.backend.enums.ShipmentStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "shipments")
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String trackingNumber;

    @Column(nullable = false, length = 100)
    private String source;

    @Column(nullable = false, length = 100)
    private String destination;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ShipmentPriority priority = ShipmentPriority.MEDIUM;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ShipmentStatus status = ShipmentStatus.CREATED;

    @Column(length = 100)
    private String cargoType;

    @Column
    private Double weight;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime expectedDelivery;
    private LocalDateTime actualDelivery;

    public Shipment() {}

    public Shipment(Long id, String trackingNumber, String source, String destination, ShipmentPriority priority, ShipmentStatus status, String cargoType, Double weight, Vehicle vehicle, LocalDateTime createdAt, LocalDateTime expectedDelivery, LocalDateTime actualDelivery) {
        this.id = id;
        this.trackingNumber = trackingNumber;
        this.source = source;
        this.destination = destination;
        this.priority = priority != null ? priority : ShipmentPriority.MEDIUM;
        this.status = status != null ? status : ShipmentStatus.CREATED;
        this.cargoType = cargoType;
        this.weight = weight;
        this.vehicle = vehicle;
        this.createdAt = createdAt;
        this.expectedDelivery = expectedDelivery;
        this.actualDelivery = actualDelivery;
    }

    public static ShipmentBuilder builder() {
        return new ShipmentBuilder();
    }

    public static class ShipmentBuilder {
        private Long id;
        private String trackingNumber;
        private String source;
        private String destination;
        private ShipmentPriority priority = ShipmentPriority.MEDIUM;
        private ShipmentStatus status = ShipmentStatus.CREATED;
        private String cargoType;
        private Double weight;
        private Vehicle vehicle;
        private LocalDateTime createdAt;
        private LocalDateTime expectedDelivery;
        private LocalDateTime actualDelivery;

        public ShipmentBuilder id(Long id) { this.id = id; return this; }
        public ShipmentBuilder trackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; return this; }
        public ShipmentBuilder source(String source) { this.source = source; return this; }
        public ShipmentBuilder destination(String destination) { this.destination = destination; return this; }
        public ShipmentBuilder priority(ShipmentPriority priority) { this.priority = priority; return this; }
        public ShipmentBuilder status(ShipmentStatus status) { this.status = status; return this; }
        public ShipmentBuilder cargoType(String cargoType) { this.cargoType = cargoType; return this; }
        public ShipmentBuilder weight(Double weight) { this.weight = weight; return this; }
        public ShipmentBuilder vehicle(Vehicle vehicle) { this.vehicle = vehicle; return this; }
        public ShipmentBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public ShipmentBuilder expectedDelivery(LocalDateTime expectedDelivery) { this.expectedDelivery = expectedDelivery; return this; }
        public ShipmentBuilder actualDelivery(LocalDateTime actualDelivery) { this.actualDelivery = actualDelivery; return this; }

        public Shipment build() {
            return new Shipment(id, trackingNumber, source, destination, priority, status, cargoType, weight, vehicle, createdAt, expectedDelivery, actualDelivery);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public ShipmentPriority getPriority() { return priority; }
    public void setPriority(ShipmentPriority priority) { this.priority = priority; }
    public ShipmentStatus getStatus() { return status; }
    public void setStatus(ShipmentStatus status) { this.status = status; }
    public String getCargoType() { return cargoType; }
    public void setCargoType(String cargoType) { this.cargoType = cargoType; }
    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }
    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getExpectedDelivery() { return expectedDelivery; }
    public void setExpectedDelivery(LocalDateTime expectedDelivery) { this.expectedDelivery = expectedDelivery; }
    public LocalDateTime getActualDelivery() { return actualDelivery; }
    public void setActualDelivery(LocalDateTime actualDelivery) { this.actualDelivery = actualDelivery; }
}
