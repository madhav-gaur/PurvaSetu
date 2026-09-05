package com.nerlogistics.backend.service;

import com.nerlogistics.backend.dto.vehicle.LocationUpdateRequest;
import com.nerlogistics.backend.dto.vehicle.VehicleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GPSService {

    private final VehicleService vehicleService;

    /**
     * Calculates great-circle distance between two coordinates in kilometers using Haversine formula
     */
    public double calculateDistanceKm(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the Earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    /**
     * Advances vehicle position along a given list of [latitude, longitude] route waypoints
     */
    public VehicleResponse advanceVehicleOnRoute(Long vehicleId, List<List<Double>> routeCoordinates, int stepIndex, double speedKmh) {
        if (routeCoordinates == null || routeCoordinates.isEmpty()) {
            return vehicleService.getVehicleById(vehicleId);
        }

        int index = Math.min(stepIndex, routeCoordinates.size() - 1);
        List<Double> point = routeCoordinates.get(index);
        double lat = point.get(0);
        double lon = point.get(1);

        LocationUpdateRequest request = LocationUpdateRequest.builder()
                .latitude(lat)
                .longitude(lon)
                .speed(speedKmh)
                .timestamp(LocalDateTime.now())
                .build();

        return vehicleService.updateLocation(vehicleId, request);
    }
}
