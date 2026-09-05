package com.nerlogistics.backend.dto.shipment;

import com.nerlogistics.backend.dto.vehicle.VehicleResponse;
import com.nerlogistics.backend.enums.ShipmentPriority;
import com.nerlogistics.backend.enums.ShipmentStatus;
import java.time.LocalDateTime;

public class ShipmentResponse {
    private Long id;
    private String trackingNumber;
    private String source;
    private String destination;
    private ShipmentPriority priority;
    private ShipmentStatus status;
    private String cargoType;
    private Double weight;
    private VehicleResponse vehicle;
    private LocalDateTime createdAt;
    private LocalDateTime expectedDelivery;
    private LocalDateTime actualDelivery;

    public ShipmentResponse() {}

    public ShipmentResponse(Long id, String trackingNumber, String source, String destination, ShipmentPriority priority, ShipmentStatus status, String cargoType, Double weight, VehicleResponse vehicle, LocalDateTime createdAt, LocalDateTime expectedDelivery, LocalDateTime actualDelivery) {
        this.id = id;
        this.trackingNumber = trackingNumber;
        this.source = source;
        this.destination = destination;
        this.priority = priority;
        this.status = status;
        this.cargoType = cargoType;
        this.weight = weight;
        this.vehicle = vehicle;
        this.createdAt = createdAt;
        this.expectedDelivery = expectedDelivery;
        this.actualDelivery = actualDelivery;
    }

    public static ShipmentResponseBuilder builder() { return new ShipmentResponseBuilder(); }
    public static class ShipmentResponseBuilder {
        private Long id;
        private String trackingNumber;
        private String source;
        private String destination;
        private ShipmentPriority priority;
        private ShipmentStatus status;
        private String cargoType;
        private Double weight;
        private VehicleResponse vehicle;
        private LocalDateTime createdAt;
        private LocalDateTime expectedDelivery;
        private LocalDateTime actualDelivery;

        public ShipmentResponseBuilder id(Long id) { this.id = id; return this; }
        public ShipmentResponseBuilder trackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; return this; }
        public ShipmentResponseBuilder source(String source) { this.source = source; return this; }
        public ShipmentResponseBuilder destination(String destination) { this.destination = destination; return this; }
        public ShipmentResponseBuilder priority(ShipmentPriority priority) { this.priority = priority; return this; }
        public ShipmentResponseBuilder status(ShipmentStatus status) { this.status = status; return this; }
        public ShipmentResponseBuilder cargoType(String cargoType) { this.cargoType = cargoType; return this; }
        public ShipmentResponseBuilder weight(Double weight) { this.weight = weight; return this; }
        public ShipmentResponseBuilder vehicle(VehicleResponse vehicle) { this.vehicle = vehicle; return this; }
        public ShipmentResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public ShipmentResponseBuilder expectedDelivery(LocalDateTime expectedDelivery) { this.expectedDelivery = expectedDelivery; return this; }
        public ShipmentResponseBuilder actualDelivery(LocalDateTime actualDelivery) { this.actualDelivery = actualDelivery; return this; }

        public ShipmentResponse build() {
            return new ShipmentResponse(id, trackingNumber, source, destination, priority, status, cargoType, weight, vehicle, createdAt, expectedDelivery, actualDelivery);
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
    public VehicleResponse getVehicle() { return vehicle; }
    public void setVehicle(VehicleResponse vehicle) { this.vehicle = vehicle; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getExpectedDelivery() { return expectedDelivery; }
    public void setExpectedDelivery(LocalDateTime expectedDelivery) { this.expectedDelivery = expectedDelivery; }
    public LocalDateTime getActualDelivery() { return actualDelivery; }
    public void setActualDelivery(LocalDateTime actualDelivery) { this.actualDelivery = actualDelivery; }
}
