package com.nerlogistics.backend.dto.shipment;

import jakarta.validation.constraints.NotNull;

public class AssignVehicleRequest {
    @NotNull(message = "Vehicle ID is required")
    private Long vehicleId;

    public AssignVehicleRequest() {}
    public AssignVehicleRequest(Long vehicleId) { this.vehicleId = vehicleId; }

    public static AssignVehicleRequestBuilder builder() { return new AssignVehicleRequestBuilder(); }
    public static class AssignVehicleRequestBuilder {
        private Long vehicleId;
        public AssignVehicleRequestBuilder vehicleId(Long vehicleId) { this.vehicleId = vehicleId; return this; }
        public AssignVehicleRequest build() { return new AssignVehicleRequest(vehicleId); }
    }

    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }
}
