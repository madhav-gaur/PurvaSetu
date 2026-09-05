package com.nerlogistics.backend.dto.shipment;

import com.nerlogistics.backend.enums.ShipmentStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateStatusRequest {
    @NotNull(message = "Status is required")
    private ShipmentStatus status;

    public UpdateStatusRequest() {}
    public UpdateStatusRequest(ShipmentStatus status) { this.status = status; }

    public static UpdateStatusRequestBuilder builder() { return new UpdateStatusRequestBuilder(); }
    public static class UpdateStatusRequestBuilder {
        private ShipmentStatus status;
        public UpdateStatusRequestBuilder status(ShipmentStatus status) { this.status = status; return this; }
        public UpdateStatusRequest build() { return new UpdateStatusRequest(status); }
    }

    public ShipmentStatus getStatus() { return status; }
    public void setStatus(ShipmentStatus status) { this.status = status; }
}
