package com.nerlogistics.backend.dto.route;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeoJsonGeometry {
    @Builder.Default
    private String type = "LineString";
    
    // Coordinates as list of [longitude, latitude] pairs from OSRM
    private List<List<Double>> coordinates;
}
