package com.nerlogistics.backend.dto.simulation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimulationTriggerRequest {
    private String scenario; // "HEAVY_RAIN", "LANDSLIDE_BLOCKAGE", "ROAD_CLOSURE", "RESET", "STEP_ADVANCE", "FULL_DEMO"
    private String targetLocation; // e.g., "Sonapur, Meghalaya (NH-6)"
    private Double severity; // 0.0 to 1.0
    private Long shipmentId;
    private Long vehicleId;
}
