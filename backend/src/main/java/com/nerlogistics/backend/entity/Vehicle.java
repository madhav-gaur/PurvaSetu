package com.nerlogistics.backend.entity;

import com.nerlogistics.backend.enums.VehicleStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String vehicleNumber;

    @Column(length = 50)
    private String vehicleType;

    @Column(length = 100)
    private String driver;

    @Column(length = 20)
    private String driverPhone;

    @Column(nullable = false)
    private Double currentLatitude;

    @Column(nullable = false)
    private Double currentLongitude;

    @Column(nullable = false)
    private Double speed = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private VehicleStatus status = VehicleStatus.AVAILABLE;

    @Column(length = 50)
    private String currentShipment;

    private LocalDateTime lastUpdated;

    public Vehicle() {}

    public Vehicle(Long id, String vehicleNumber, String vehicleType, String driver, String driverPhone, Double currentLatitude, Double currentLongitude, Double speed, VehicleStatus status, String currentShipment, LocalDateTime lastUpdated) {
        this.id = id;
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.driver = driver;
        this.driverPhone = driverPhone;
        this.currentLatitude = currentLatitude;
        this.currentLongitude = currentLongitude;
        this.speed = speed != null ? speed : 0.0;
        this.status = status != null ? status : VehicleStatus.AVAILABLE;
        this.currentShipment = currentShipment;
        this.lastUpdated = lastUpdated != null ? lastUpdated : LocalDateTime.now();
    }

    public static VehicleBuilder builder() {
        return new VehicleBuilder();
    }

    public static class VehicleBuilder {
        private Long id;
        private String vehicleNumber;
        private String vehicleType;
        private String driver;
        private String driverPhone;
        private Double currentLatitude;
        private Double currentLongitude;
        private Double speed = 0.0;
        private VehicleStatus status = VehicleStatus.AVAILABLE;
        private String currentShipment;
        private LocalDateTime lastUpdated;

        public VehicleBuilder id(Long id) { this.id = id; return this; }
        public VehicleBuilder vehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; return this; }
        public VehicleBuilder vehicleType(String vehicleType) { this.vehicleType = vehicleType; return this; }
        public VehicleBuilder driver(String driver) { this.driver = driver; return this; }
        public VehicleBuilder driverPhone(String driverPhone) { this.driverPhone = driverPhone; return this; }
        public VehicleBuilder currentLatitude(Double currentLatitude) { this.currentLatitude = currentLatitude; return this; }
        public VehicleBuilder currentLongitude(Double currentLongitude) { this.currentLongitude = currentLongitude; return this; }
        public VehicleBuilder speed(Double speed) { this.speed = speed; return this; }
        public VehicleBuilder status(VehicleStatus status) { this.status = status; return this; }
        public VehicleBuilder currentShipment(String currentShipment) { this.currentShipment = currentShipment; return this; }
        public VehicleBuilder lastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; return this; }

        public Vehicle build() {
            return new Vehicle(id, vehicleNumber, vehicleType, driver, driverPhone, currentLatitude, currentLongitude, speed, status, currentShipment, lastUpdated);
        }
    }

    @PrePersist
    @PreUpdate
    public void onUpdate() {
        this.lastUpdated = LocalDateTime.now();
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
