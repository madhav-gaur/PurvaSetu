package com.nerlogistics.backend.dto.vehicle;

import com.nerlogistics.backend.enums.VehicleStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class VehicleRequest {
    @NotBlank(message = "Vehicle number is required")
    private String vehicleNumber;
    private String vehicleType;
    private String driver;
    private String driverPhone;

    @NotNull(message = "Current latitude is required")
    private Double currentLatitude;

    @NotNull(message = "Current longitude is required")
    private Double currentLongitude;

    private Double speed;
    private VehicleStatus status;
    private String currentShipment;

    public VehicleRequest() {}

    public VehicleRequest(String vehicleNumber, String vehicleType, String driver, String driverPhone, Double currentLatitude, Double currentLongitude, Double speed, VehicleStatus status, String currentShipment) {
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.driver = driver;
        this.driverPhone = driverPhone;
        this.currentLatitude = currentLatitude;
        this.currentLongitude = currentLongitude;
        this.speed = speed;
        this.status = status;
        this.currentShipment = currentShipment;
    }

    public static VehicleRequestBuilder builder() { return new VehicleRequestBuilder(); }
    public static class VehicleRequestBuilder {
        private String vehicleNumber;
        private String vehicleType;
        private String driver;
        private String driverPhone;
        private Double currentLatitude;
        private Double currentLongitude;
        private Double speed;
        private VehicleStatus status;
        private String currentShipment;

        public VehicleRequestBuilder vehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; return this; }
        public VehicleRequestBuilder vehicleType(String vehicleType) { this.vehicleType = vehicleType; return this; }
        public VehicleRequestBuilder driver(String driver) { this.driver = driver; return this; }
        public VehicleRequestBuilder driverPhone(String driverPhone) { this.driverPhone = driverPhone; return this; }
        public VehicleRequestBuilder currentLatitude(Double currentLatitude) { this.currentLatitude = currentLatitude; return this; }
        public VehicleRequestBuilder currentLongitude(Double currentLongitude) { this.currentLongitude = currentLongitude; return this; }
        public VehicleRequestBuilder speed(Double speed) { this.speed = speed; return this; }
        public VehicleRequestBuilder status(VehicleStatus status) { this.status = status; return this; }
        public VehicleRequestBuilder currentShipment(String currentShipment) { this.currentShipment = currentShipment; return this; }

        public VehicleRequest build() {
            return new VehicleRequest(vehicleNumber, vehicleType, driver, driverPhone, currentLatitude, currentLongitude, speed, status, currentShipment);
        }
    }

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
}
