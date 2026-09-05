package com.nerlogistics.backend.service;

import com.nerlogistics.backend.dto.weather.WeatherResponseDto;
import com.nerlogistics.backend.entity.WeatherObservation;
import com.nerlogistics.backend.enums.RiskLevel;
import com.nerlogistics.backend.integration.WeatherClient;
import com.nerlogistics.backend.repository.WeatherObservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WeatherClient weatherClient;
    private final WeatherObservationRepository weatherObservationRepository;

    @Transactional
    public WeatherResponseDto getCurrentWeather(Double latitude, Double longitude, String locationName) {
        if (latitude == null) latitude = 26.1445; // Default: Guwahati
        if (longitude == null) longitude = 91.7362;

        WeatherObservation obs = weatherClient.fetchWeather(latitude, longitude);
        obs = weatherObservationRepository.save(obs);

        return mapToDto(obs, locationName);
    }

    public List<WeatherResponseDto> getRecentObservations() {
        return weatherObservationRepository.findTop50ByOrderByObservedAtDesc().stream()
                .map(obs -> mapToDto(obs, "NER Regional Telemetry"))
                .toList();
    }

    public WeatherResponseDto mapToDto(WeatherObservation obs, String locationName) {
        if (obs == null) return null;

        // Calculate flood and landslide risk from rainfall accumulation
        double r3h = obs.getRainfall3Hour() != null ? obs.getRainfall3Hour() : 0.0;
        double r1d = obs.getRainfall1Day() != null ? obs.getRainfall1Day() : 0.0;
        double wind = obs.getWindSpeed() != null ? obs.getWindSpeed() : 0.0;

        double floodRisk = Math.min(1.0, (r1d / 120.0) * 0.7 + (r3h / 40.0) * 0.3);
        double landslideRisk = Math.min(1.0, (r1d / 100.0) * 0.5 + (r3h / 30.0) * 0.5);

        RiskLevel level;
        double maxRisk = Math.max(floodRisk, landslideRisk);
        if (maxRisk > 0.8) level = RiskLevel.CRITICAL;
        else if (maxRisk > 0.6) level = RiskLevel.HIGH;
        else if (maxRisk > 0.3) level = RiskLevel.MEDIUM;
        else level = RiskLevel.LOW;

        String condition = decodeWeatherCode(obs.getWeatherCode());

        return WeatherResponseDto.builder()
                .latitude(obs.getLatitude())
                .longitude(obs.getLongitude())
                .locationName(locationName != null ? locationName : "NER Corridor")
                .temperature(obs.getTemperature())
                .humidity(obs.getHumidity())
                .precipitation(obs.getPrecipitation())
                .rain(obs.getRain())
                .windSpeed(obs.getWindSpeed())
                .weatherCode(obs.getWeatherCode())
                .weatherCondition(condition)
                .rainfall3Hour(obs.getRainfall3Hour())
                .rainfall1Day(obs.getRainfall1Day())
                .floodRisk(Math.round(floodRisk * 100.0) / 100.0)
                .landslideRisk(Math.round(landslideRisk * 100.0) / 100.0)
                .riskLevel(level)
                .source(obs.getSource())
                .observedAt(obs.getObservedAt())
                .build();
    }

    private String decodeWeatherCode(Integer code) {
        if (code == null) return "Clear Sky";
        return switch (code) {
            case 0 -> "Clear Sky";
            case 1, 2, 3 -> "Partly Cloudy";
            case 45, 48 -> "Fog & Low Mountain Visibility";
            case 51, 53, 55 -> "Light Drizzle";
            case 61, 63 -> "Moderate Rain";
            case 65 -> "Heavy Downpour / Torrential Rain";
            case 80, 81, 82 -> "Monsoon Showers";
            case 95, 96, 99 -> "Thunderstorm with High Wind Gusts";
            default -> "Overcast / Precipitation";
        };
    }
}
