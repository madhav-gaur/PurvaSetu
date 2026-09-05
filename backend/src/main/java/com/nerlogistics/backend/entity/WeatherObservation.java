package com.nerlogistics.backend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "weather_observations", indexes = {
    @Index(name = "idx_weather_lat_lon", columnList = "latitude, longitude"),
    @Index(name = "idx_weather_observed_at", columnList = "observedAt")
})
public class WeatherObservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    private Double temperature;
    private Double humidity;
    private Double precipitation;
    private Double rain;
    private Double windSpeed;
    private Integer weatherCode;
    private Double rainfall3Hour;
    private Double rainfall1Day;

    @Column(length = 50)
    private String source = "OPEN_METEO";

    @CreationTimestamp
    private LocalDateTime observedAt;

    public WeatherObservation() {}

    public WeatherObservation(Long id, Double latitude, Double longitude, Double temperature, Double humidity, Double precipitation, Double rain, Double windSpeed, Integer weatherCode, Double rainfall3Hour, Double rainfall1Day, String source, LocalDateTime observedAt) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.temperature = temperature;
        this.humidity = humidity;
        this.precipitation = precipitation;
        this.rain = rain;
        this.windSpeed = windSpeed;
        this.weatherCode = weatherCode;
        this.rainfall3Hour = rainfall3Hour;
        this.rainfall1Day = rainfall1Day;
        this.source = source != null ? source : "OPEN_METEO";
        this.observedAt = observedAt;
    }

    public static WeatherObservationBuilder builder() {
        return new WeatherObservationBuilder();
    }

    public static class WeatherObservationBuilder {
        private Long id;
        private Double latitude;
        private Double longitude;
        private Double temperature;
        private Double humidity;
        private Double precipitation;
        private Double rain;
        private Double windSpeed;
        private Integer weatherCode;
        private Double rainfall3Hour;
        private Double rainfall1Day;
        private String source = "OPEN_METEO";
        private LocalDateTime observedAt;

        public WeatherObservationBuilder id(Long id) { this.id = id; return this; }
        public WeatherObservationBuilder latitude(Double latitude) { this.latitude = latitude; return this; }
        public WeatherObservationBuilder longitude(Double longitude) { this.longitude = longitude; return this; }
        public WeatherObservationBuilder temperature(Double temperature) { this.temperature = temperature; return this; }
        public WeatherObservationBuilder humidity(Double humidity) { this.humidity = humidity; return this; }
        public WeatherObservationBuilder precipitation(Double precipitation) { this.precipitation = precipitation; return this; }
        public WeatherObservationBuilder rain(Double rain) { this.rain = rain; return this; }
        public WeatherObservationBuilder windSpeed(Double windSpeed) { this.windSpeed = windSpeed; return this; }
        public WeatherObservationBuilder weatherCode(Integer weatherCode) { this.weatherCode = weatherCode; return this; }
        public WeatherObservationBuilder rainfall3Hour(Double rainfall3Hour) { this.rainfall3Hour = rainfall3Hour; return this; }
        public WeatherObservationBuilder rainfall1Day(Double rainfall1Day) { this.rainfall1Day = rainfall1Day; return this; }
        public WeatherObservationBuilder source(String source) { this.source = source; return this; }
        public WeatherObservationBuilder observedAt(LocalDateTime observedAt) { this.observedAt = observedAt; return this; }

        public WeatherObservation build() {
            return new WeatherObservation(id, latitude, longitude, temperature, humidity, precipitation, rain, windSpeed, weatherCode, rainfall3Hour, rainfall1Day, source, observedAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public Double getTemperature() { return temperature; }
    public void setTemperature(Double temperature) { this.temperature = temperature; }
    public Double getHumidity() { return humidity; }
    public void setHumidity(Double humidity) { this.humidity = humidity; }
    public Double getPrecipitation() { return precipitation; }
    public void setPrecipitation(Double precipitation) { this.precipitation = precipitation; }
    public Double getRain() { return rain; }
    public void setRain(Double rain) { this.rain = rain; }
    public Double getWindSpeed() { return windSpeed; }
    public void setWindSpeed(Double windSpeed) { this.windSpeed = windSpeed; }
    public Integer getWeatherCode() { return weatherCode; }
    public void setWeatherCode(Integer weatherCode) { this.weatherCode = weatherCode; }
    public Double getRainfall3Hour() { return rainfall3Hour; }
    public void setRainfall3Hour(Double rainfall3Hour) { this.rainfall3Hour = rainfall3Hour; }
    public Double getRainfall1Day() { return rainfall1Day; }
    public void setRainfall1Day(Double rainfall1Day) { this.rainfall1Day = rainfall1Day; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public LocalDateTime getObservedAt() { return observedAt; }
    public void setObservedAt(LocalDateTime observedAt) { this.observedAt = observedAt; }
}
