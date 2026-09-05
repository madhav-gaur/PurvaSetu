package com.nerlogistics.backend.controller;

import com.nerlogistics.backend.dto.risk.RiskPredictionRequest;
import com.nerlogistics.backend.dto.risk.RiskPredictionResponse;
import com.nerlogistics.backend.entity.RiskPrediction;
import com.nerlogistics.backend.entity.Route;
import com.nerlogistics.backend.repository.RiskPredictionRepository;
import com.nerlogistics.backend.service.RiskService;
import com.nerlogistics.backend.service.RouteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/risk")
@Tag(name = "Disaster Risk & AI Engine", description = "Multi-factor flood, landslide, weather, and road disruption risk predictions")
@RequiredArgsConstructor
public class RiskController {

    private final RiskService riskService;
    private final RouteService routeService;
    private final RiskPredictionRepository riskPredictionRepository;

    @GetMapping
    @Operation(summary = "Get recent risk predictions")
    public ResponseEntity<List<RiskPrediction>> getRecentRiskPredictions() {
        return ResponseEntity.ok(riskPredictionRepository.findTop20ByOrderByPredictedAtDesc());
    }

    @PostMapping("/predict")
    @Operation(summary = "Predict multi-hazard risk using AI Service / Weighted Risk Engine")
    public ResponseEntity<RiskPredictionResponse> predictRisk(@RequestBody RiskPredictionRequest request) {
        return ResponseEntity.ok(riskService.calculateRisk(request));
    }

    @GetMapping("/route/{routeId}")
    @Operation(summary = "Get evaluated risk profile for a specific route")
    public ResponseEntity<RiskPredictionResponse> getRouteRisk(@PathVariable Long routeId) {
        Route route = routeService.getRouteById(routeId);
        RiskPredictionRequest req = RiskPredictionRequest.builder()
                .latitude(route.getStartLatitude())
                .longitude(route.getStartLongitude())
                .rainfall1Day(route.getRiskScore() * 80.0)
                .roadCondition(route.getRiskScore())
                .build();
        return ResponseEntity.ok(riskService.calculateRisk(req));
    }
}
