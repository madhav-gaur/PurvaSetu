package com.nerlogistics.backend.dto.vehicle;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class LocationUpdateRequest {
    @NotNull(message = "Latitude is required")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    private Double longitude;

    private Double speed;
    private LocalDateTime timestamp;

    public LocationUpdateRequest() {}
    public LocationUpdateRequest(Double latitude, Double longitude, Double speed, LocalDateTime timestamp) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
        this.timestamp = timestamp;
    }

    public static LocationUpdateRequestBuilder builder() { return new LocationUpdateRequestBuilder(); }
    public static class LocationUpdateRequestBuilder {
        private Double latitude;
        private Double longitude;
        private Double speed;
        private LocalDateTime timestamp;

        public LocationUpdateRequestBuilder latitude(Double latitude) { this.latitude = latitude; return this; }
        public LocationUpdateRequestBuilder longitude(Double longitude) { this.longitude = longitude; return this; }
        public LocationUpdateRequestBuilder speed(Double speed) { this.speed = speed; return this; }
        public LocationUpdateRequestBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }

        public LocationUpdateRequest build() {
            return new LocationUpdateRequest(latitude, longitude, speed, timestamp);
        }
    }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public Double getSpeed() { return speed; }
    public void setSpeed(Double speed) { this.speed = speed; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
