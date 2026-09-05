package com.nerlogistics.backend.dto.shipment;

import com.nerlogistics.backend.enums.ShipmentPriority;
import com.nerlogistics.backend.enums.ShipmentStatus;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class ShipmentRequest {
    private String trackingNumber;

    @NotBlank(message = "Source location is required")
    private String source;

    @NotBlank(message = "Destination location is required")
    private String destination;

    private ShipmentPriority priority;
    private ShipmentStatus status;
    private String cargoType;
    private Double weight;
    private Long vehicleId;
    private LocalDateTime expectedDelivery;

    public ShipmentRequest() {}

    public ShipmentRequest(String trackingNumber, String source, String destination, ShipmentPriority priority, ShipmentStatus status, String cargoType, Double weight, Long vehicleId, LocalDateTime expectedDelivery) {
        this.trackingNumber = trackingNumber;
        this.source = source;
        this.destination = destination;
        this.priority = priority;
        this.status = status;
        this.cargoType = cargoType;
        this.weight = weight;
        this.vehicleId = vehicleId;
        this.expectedDelivery = expectedDelivery;
    }

    public static ShipmentRequestBuilder builder() { return new ShipmentRequestBuilder(); }
    public static class ShipmentRequestBuilder {
        private String trackingNumber;
        private String source;
        private String destination;
        private ShipmentPriority priority = ShipmentPriority.MEDIUM;
        private ShipmentStatus status = ShipmentStatus.CREATED;
        private String cargoType;
        private Double weight;
        private Long vehicleId;
        private LocalDateTime expectedDelivery;

        public ShipmentRequestBuilder trackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; return this; }
        public ShipmentRequestBuilder source(String source) { this.source = source; return this; }
        public ShipmentRequestBuilder destination(String destination) { this.destination = destination; return this; }
        public ShipmentRequestBuilder priority(ShipmentPriority priority) { this.priority = priority; return this; }
        public ShipmentRequestBuilder status(ShipmentStatus status) { this.status = status; return this; }
        public ShipmentRequestBuilder cargoType(String cargoType) { this.cargoType = cargoType; return this; }
        public ShipmentRequestBuilder weight(Double weight) { this.weight = weight; return this; }
        public ShipmentRequestBuilder vehicleId(Long vehicleId) { this.vehicleId = vehicleId; return this; }
        public ShipmentRequestBuilder expectedDelivery(LocalDateTime expectedDelivery) { this.expectedDelivery = expectedDelivery; return this; }

        public ShipmentRequest build() {
            return new ShipmentRequest(trackingNumber, source, destination, priority, status, cargoType, weight, vehicleId, expectedDelivery);
        }
    }

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
    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }
    public LocalDateTime getExpectedDelivery() { return expectedDelivery; }
    public void setExpectedDelivery(LocalDateTime expectedDelivery) { this.expectedDelivery = expectedDelivery; }
}
