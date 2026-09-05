package com.nerlogistics.backend.controller;

import com.nerlogistics.backend.dto.route.RerouteRequest;
import com.nerlogistics.backend.dto.route.RouteCalculationRequest;
import com.nerlogistics.backend.dto.route.RouteComparisonResponse;
import com.nerlogistics.backend.dto.route.RouteResponseDto;
import com.nerlogistics.backend.entity.Route;
import com.nerlogistics.backend.enums.ShipmentPriority;
import com.nerlogistics.backend.service.RouteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
@Tag(name = "Smart Routing & Pathfinding", description = "Risk-aware route calculation, multi-route comparison, and dynamic rerouting")
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;

    @GetMapping
    @Operation(summary = "Get route between default or queried endpoints")
    public ResponseEntity<RouteComparisonResponse> getRoute(
            @RequestParam(required = false, defaultValue = "26.1445") Double startLat,
            @RequestParam(required = false, defaultValue = "91.7362") Double startLng,
            @RequestParam(required = false, defaultValue = "24.8333") Double destLat,
            @RequestParam(required = false, defaultValue = "92.7789") Double destLng,
            @RequestParam(required = false, defaultValue = "MEDIUM") ShipmentPriority priority
    ) {
        RouteCalculationRequest req = RouteCalculationRequest.builder()
                .startLatitude(startLat)
                .startLongitude(startLng)
                .destinationLatitude(destLat)
                .destinationLongitude(destLng)
                .originName("Guwahati, Assam")
                .destinationName("Silchar, Assam")
                .priority(priority)
                .build();
        return ResponseEntity.ok(routeService.calculateAndCompareRoutes(req));
    }

    @PostMapping("/calculate")
    @Operation(summary = "Calculate and compare multiple route alternatives with risk scoring")
    public ResponseEntity<RouteComparisonResponse> calculateRoutes(@Valid @RequestBody RouteCalculationRequest request) {
        return ResponseEntity.ok(routeService.calculateAndCompareRoutes(request));
    }

    @PostMapping("/optimize")
    @Operation(summary = "Alias for route optimization with multi-hazard penalties")
    public ResponseEntity<RouteComparisonResponse> optimizeRoutes(@Valid @RequestBody RouteCalculationRequest request) {
        return ResponseEntity.ok(routeService.calculateAndCompareRoutes(request));
    }

    @PostMapping("/reroute")
    @Operation(summary = "Trigger dynamic rerouting for an active shipment")
    public ResponseEntity<RouteResponseDto> rerouteShipment(@Valid @RequestBody RerouteRequest request) {
        return ResponseEntity.ok(routeService.rerouteShipment(request));
    }

    @GetMapping("/history")
    @Operation(summary = "Get historical calculated routes")
    public ResponseEntity<List<Route>> getAllRoutes() {
        return ResponseEntity.ok(routeService.getAllRoutes());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get route by ID")
    public ResponseEntity<Route> getRouteById(@PathVariable Long id) {
        return ResponseEntity.ok(routeService.getRouteById(id));
    }
}
