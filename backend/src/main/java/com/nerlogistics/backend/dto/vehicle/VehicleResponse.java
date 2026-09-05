package com.nerlogistics.backend.dto.vehicle;

import com.nerlogistics.backend.enums.VehicleStatus;
import java.time.LocalDateTime;

public class VehicleResponse {
    private Long id;
    private String vehicleNumber;
    private String vehicleType;
    private String driver;
    private String driverPhone;
    private Double currentLatitude;
    private Double currentLongitude;
    private Double speed;
    private VehicleStatus status;
    private String currentShipment;
    private LocalDateTime lastUpdated;

    public VehicleResponse() {}

    public VehicleResponse(Long id, String vehicleNumber, String vehicleType, String driver, String driverPhone, Double currentLatitude, Double currentLongitude, Double speed, VehicleStatus status, String currentShipment, LocalDateTime lastUpdated) {
        this.id = id;
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.driver = driver;
        this.driverPhone = driverPhone;
        this.currentLatitude = currentLatitude;
        this.currentLongitude = currentLongitude;
        this.speed = speed;
        this.status = status;
        this.currentShipment = currentShipment;
        this.lastUpdated = lastUpdated;
    }

    public static VehicleResponseBuilder builder() { return new VehicleResponseBuilder(); }
    public static class VehicleResponseBuilder {
        private Long id;
        private String vehicleNumber;
        private String vehicleType;
        private String driver;
        private String driverPhone;
        private Double currentLatitude;
        private Double currentLongitude;
        private Double speed;
        private VehicleStatus status;
        private String currentShipment;
        private LocalDateTime lastUpdated;

        public VehicleResponseBuilder id(Long id) { this.id = id; return this; }
        public VehicleResponseBuilder vehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; return this; }
        public VehicleResponseBuilder vehicleType(String vehicleType) { this.vehicleType = vehicleType; return this; }
        public VehicleResponseBuilder driver(String driver) { this.driver = driver; return this; }
        public VehicleResponseBuilder driverPhone(String driverPhone) { this.driverPhone = driverPhone; return this; }
        public VehicleResponseBuilder currentLatitude(Double currentLatitude) { this.currentLatitude = currentLatitude; return this; }
        public VehicleResponseBuilder currentLongitude(Double currentLongitude) { this.currentLongitude = currentLongitude; return this; }
        public VehicleResponseBuilder speed(Double speed) { this.speed = speed; return this; }
        public VehicleResponseBuilder status(VehicleStatus status) { this.status = status; return this; }
        public VehicleResponseBuilder currentShipment(String currentShipment) { this.currentShipment = currentShipment; return this; }
        public VehicleResponseBuilder lastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; return this; }

        public VehicleResponse build() {
            return new VehicleResponse(id, vehicleNumber, vehicleType, driver, driverPhone, currentLatitude, currentLongitude, speed, status, currentShipment, lastUpdated);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }
    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }
    public String getDriver() { return driver; }
    public void setDriver(String driver) { this.driver = driver; }
    public String getDriverPhone() { return driverPhone; }
    public void setDriverPhone(String driverPhone) { this.driverPhone = driverPhone; }
    public Double getCurrentLatitude() { return currentLatitude; }
    public void setCurrentLatitude(Double currentLatitude) { this.currentLatitude = currentLatitude; }
    public Double getCurrentLongitude() { return currentLongitude; }
    public void setCurrentLongitude(Double currentLongitude) { this.currentLongitude = currentLongitude; }
    public Double getSpeed() { return speed; }
    public void setSpeed(Double speed) { this.speed = speed; }
    public VehicleStatus getStatus() { return status; }
    public void setStatus(VehicleStatus status) { this.status = status; }
    public String getCurrentShipment() { return currentShipment; }
    public void setCurrentShipment(String currentShipment) { this.currentShipment = currentShipment; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
}
