package com.nerlogistics.backend.dto.weather;

import com.nerlogistics.backend.enums.RiskLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherResponseDto {
    private Double latitude;
    private Double longitude;
    private String locationName;
    private Double temperature;
    private Double humidity;
    private Double precipitation;
    private Double rain;
    private Double windSpeed;
    private Integer weatherCode;
    private String weatherCondition;
    private Double rainfall3Hour;
    private Double rainfall1Day;
    private Double floodRisk;
    private Double landslideRisk;
    private RiskLevel riskLevel;
    private String source;
    private LocalDateTime observedAt;
}
