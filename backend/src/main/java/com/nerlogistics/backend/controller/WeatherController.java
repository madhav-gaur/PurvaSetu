package com.nerlogistics.backend.controller;

import com.nerlogistics.backend.dto.weather.WeatherResponseDto;
import com.nerlogistics.backend.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/weather")
@Tag(name = "Weather & Rainfall Telemetry", description = "Real-time Open-Meteo weather observations, 3h/24h rainfall accumulation, and hazard radar")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping
    @Operation(summary = "Get current weather for default NER regional hub (Guwahati)")
    public ResponseEntity<WeatherResponseDto> getDefaultWeather() {
        return ResponseEntity.ok(weatherService.getCurrentWeather(26.1445, 91.7362, "Guwahati Logistics Hub, Assam"));
    }

    @GetMapping("/current")
    @Operation(summary = "Get current real-time weather for specific coordinates")
    public ResponseEntity<WeatherResponseDto> getCurrentWeather(
            @RequestParam(required = false, defaultValue = "26.1445") Double latitude,
            @RequestParam(required = false, defaultValue = "91.7362") Double longitude,
            @RequestParam(required = false, defaultValue = "NER Corridor") String locationName
    ) {
        return ResponseEntity.ok(weatherService.getCurrentWeather(latitude, longitude, locationName));
    }

    @GetMapping("/history")
    @Operation(summary = "Get recent weather observations stored in database")
    public ResponseEntity<List<WeatherResponseDto>> getWeatherHistory() {
        return ResponseEntity.ok(weatherService.getRecentObservations());
    }
}
