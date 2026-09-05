package com.nerlogistics.backend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "vehicle_locations", indexes = {
    @Index(name = "idx_vehicle_time", columnList = "vehicleId, timestamp")
})
public class VehicleLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long vehicleId;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private Double speed;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime timestamp;

    public VehicleLocation() {}

    public VehicleLocation(Long id, Long vehicleId, Double latitude, Double longitude, Double speed, LocalDateTime timestamp) {
        this.id = id;
        this.vehicleId = vehicleId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
        this.timestamp = timestamp;
    }

    public static VehicleLocationBuilder builder() {
        return new VehicleLocationBuilder();
    }

    public static class VehicleLocationBuilder {
        private Long id;
        private Long vehicleId;
        private Double latitude;
        private Double longitude;
        private Double speed;
        private LocalDateTime timestamp;

        public VehicleLocationBuilder id(Long id) { this.id = id; return this; }
        public VehicleLocationBuilder vehicleId(Long vehicleId) { this.vehicleId = vehicleId; return this; }
        public VehicleLocationBuilder latitude(Double latitude) { this.latitude = latitude; return this; }
        public VehicleLocationBuilder longitude(Double longitude) { this.longitude = longitude; return this; }
        public VehicleLocationBuilder speed(Double speed) { this.speed = speed; return this; }
        public VehicleLocationBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }

        public VehicleLocation build() {
            return new VehicleLocation(id, vehicleId, latitude, longitude, speed, timestamp);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public Double getSpeed() { return speed; }
    public void setSpeed(Double speed) { this.speed = speed; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
