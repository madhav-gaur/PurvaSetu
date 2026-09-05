package com.nerlogistics.backend.service;

import com.nerlogistics.backend.dto.route.RouteCalculationRequest;
import com.nerlogistics.backend.dto.route.RouteComparisonResponse;
import com.nerlogistics.backend.dto.route.RouteResponseDto;
import com.nerlogistics.backend.dto.shipment.ShipmentRequest;
import com.nerlogistics.backend.dto.shipment.ShipmentResponse;
import com.nerlogistics.backend.dto.simulation.EmergencyDemoStatusDto;
import com.nerlogistics.backend.dto.simulation.SimulationTriggerRequest;
import com.nerlogistics.backend.dto.vehicle.LocationUpdateRequest;
import com.nerlogistics.backend.dto.vehicle.VehicleRequest;
import com.nerlogistics.backend.dto.vehicle.VehicleResponse;
import com.nerlogistics.backend.entity.Alert;
import com.nerlogistics.backend.entity.HazardEvent;
import com.nerlogistics.backend.entity.RoadReport;
import com.nerlogistics.backend.enums.*;
import com.nerlogistics.backend.repository.AlertRepository;
import com.nerlogistics.backend.repository.HazardEventRepository;
import com.nerlogistics.backend.repository.RoadReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
@RequiredArgsConstructor
public class SimulationService {

    private final VehicleService vehicleService;
    private final ShipmentService shipmentService;
    private final RouteService routeService;
    private final AlertRepository alertRepository;
    private final HazardEventRepository hazardEventRepository;
    private final RoadReportRepository roadReportRepository;

    private static final AtomicInteger demoStep = new AtomicInteger(1);
    private static Long demoShipmentId = null;
    private static Long demoVehicleId = null;
    private static RouteComparisonResponse cachedDemoRoutes = null;

    @Transactional
    public EmergencyDemoStatusDto runStep(int stepNumber) {
        demoStep.set(stepNumber);
        List<String> logs = new ArrayList<>();
        String title;
        String desc;
        String stage;

        switch (stepNumber) {
            case 1:
                title = "Step 1: Emergency Medicine Shipment Created";
                desc = "Created CRITICAL medical consignment: 450 vials of Antivenom & Pediatric Vaccines for Silchar District Hospital.";
                stage = "PLANNING";
                logs.add("System initialized priority: CRITICAL");
                logs.add("Origin: Guwahati Medical College & Hospital Hub (GMCH)");
                logs.add("Destination: Silchar Medical College, Cachar District");

                ShipmentResponse s1 = shipmentService.createShipment(ShipmentRequest.builder()
                        .trackingNumber("NER-EMERGENCY-MED-901")
                        .source("Guwahati Logistics Hub, Assam")
                        .destination("Silchar Medical Hub, Assam")
                        .priority(ShipmentPriority.CRITICAL)
                        .cargoType("Emergency Vaccines & Critical Life-Saving Medical Rations")
                        .weight(3.5)
                        .expectedDelivery(LocalDateTime.now().plusHours(7))
                        .build());
                demoShipmentId = s1.getId();
                break;

            case 2:
                title = "Step 2: Assign GPS-Monitored Refrigerated Truck";
                desc = "Assigned Cold-Chain Reefer Vehicle AS-01-GC-4412 (Driver: Manash Kalita).";
                stage = "PLANNING";

                VehicleResponse v = vehicleService.getAllVehicles().stream()
                        .filter(veh -> "AS-01-GC-4412".equals(veh.getVehicleNumber()))
                        .findFirst()
                        .orElseGet(() -> vehicleService.createVehicle(VehicleRequest.builder()
                                .vehicleNumber("AS-01-GC-4412")
                                .vehicleType("Ashok Leyland 1616 Cold-Chain Reefer")
                                .driver("Manash Kalita")
                                .driverPhone("+91 94350 11223")
                                .currentLatitude(26.1445)
                                .currentLongitude(91.7362)
                                .speed(0.0)
                                .status(VehicleStatus.IN_TRANSIT)
                                .build()));
                demoVehicleId = v.getId();

                if (demoShipmentId != null) {
                    shipmentService.assignVehicle(demoShipmentId, demoVehicleId);
                }
                logs.add("Vehicle assigned to shipment: AS-01-GC-4412");
                logs.add("GPS Telemetry Ping verified: Lat 26.1445, Lon 91.7362");
                break;

            case 3:
            case 4:
            case 5:
                title = "Step 3-5: Vehicle Departs Guwahati & Route Generated";
                desc = "Route calculated via NH-6 Meghalaya corridor (298 km). Open-Meteo weather telemetry active.";
                stage = "IN_TRANSIT";

                vehicleService.updateLocation(demoVehicleId != null ? demoVehicleId : 1L, LocationUpdateRequest.builder()
                        .latitude(25.9800)
                        .longitude(91.8200)
                        .speed(54.0)
                        .timestamp(LocalDateTime.now())
                        .build());

                cachedDemoRoutes = routeService.calculateAndCompareRoutes(RouteCalculationRequest.builder()
                        .startLatitude(26.1445)
                        .startLongitude(91.7362)
                        .destinationLatitude(24.8333)
                        .destinationLongitude(92.7789)
                        .originName("Guwahati, Assam")
                        .destinationName("Silchar, Assam")
                        .priority(ShipmentPriority.CRITICAL)
                        .shipmentId(demoShipmentId)
                        .vehicleId(demoVehicleId)
                        .build());

                logs.add("Primary corridor generated: NH-6 via Jowai-Sonapur ghat pass");
                logs.add("Initial baseline transit duration: 6h 15m");
                logs.add("Open-Meteo observation: Light rain (8.4 mm/h), moderate saturation");
                break;

            case 6:
            case 7:
            case 8:
                title = "Step 6-8: Monsoon Downpour & Landslide Hazard Detected!";
                desc = "IMD alerts 88mm torrential rain at Sonapur Ghat. Road report filed: active mudslide blocking NH-6. Risk surges to 84%!";
                stage = "DISASTER_DETECTED";

                // Introduce Hazard Event
                HazardEvent hazard = HazardEvent.builder()
                        .type(HazardType.LANDSLIDE)
                        .latitude(25.1200)
                        .longitude(92.3800)
                        .severity(Severity.CRITICAL)
                        .description("Major mudslide and boulder fall on NH-6 near Sonapur Tunnel. Road blocked.")
                        .source("IMD / ASDMA Telemetry")
                        .status("ACTIVE")
                        .occurredAt(LocalDateTime.now())
                        .expiresAt(LocalDateTime.now().plusHours(24))
                        .build();
                hazardEventRepository.save(hazard);

                // Introduce Field Report
                RoadReport report = RoadReport.builder()
                        .reporter("Officer Baruah (BRO Patrol)")
                        .latitude(25.1200)
                        .longitude(92.3800)
                        .type(ReportType.LANDSLIDE)
                        .severity(Severity.CRITICAL)
                        .status(ReportStatus.VERIFIED)
                        .description("NH-6 impassable for heavy trucks. Inundation & debris clearing underway.")
                        .photoUrl("https://images.unsplash.com/photo-1547683905-f686c993aae5?auto=format&fit=crop&w=600&q=80")
                        .createdAt(LocalDateTime.now())
                        .verifiedAt(LocalDateTime.now())
                        .build();
                roadReportRepository.save(report);

                // Introduce Critical Alert
                Alert alert = Alert.builder()
                        .type(AlertType.LANDSLIDE_WARNING)
                        .severity(Severity.CRITICAL)
                        .title("CRITICAL HAZARD: Landslide Obstruction on NH-6")
                        .message("Severe debris flow reported on primary route for Medicine Shipment NER-EMERGENCY-MED-901. Immediate rerouting recommended.")
                        .latitude(25.1200)
                        .longitude(92.3800)
                        .acknowledged(false)
                        .createdAt(LocalDateTime.now())
                        .build();
                alertRepository.save(alert);

                // Re-evaluate routes with elevated hazard
                cachedDemoRoutes = routeService.calculateAndCompareRoutes(RouteCalculationRequest.builder()
                        .startLatitude(25.9800)
                        .startLongitude(91.8200)
                        .destinationLatitude(24.8333)
                        .destinationLongitude(92.7789)
                        .originName("Guwahati-Meghalaya Border")
                        .destinationName("Silchar, Assam")
                        .priority(ShipmentPriority.CRITICAL)
                        .shipmentId(demoShipmentId)
                        .vehicleId(demoVehicleId)
                        .build());

                logs.add("⚠️ 88mm torrential precipitation recorded on Sonapur pass");
                logs.add("🚨 AI Risk Engine evaluated NH-6 Corridor Risk: 84% [CRITICAL DANGER]");
                logs.add("📢 Emergency Alert dispatched to Operator Console & Driver Terminal");
                break;

            case 9:
            case 10:
            case 11:
                title = "Step 9-11: AI Calculates & Recommends Safer Bypass Route";
                desc = "AI Engine selects Northern Bypass (NH-27 via Lumding-Haflong). 78% less hazard probability. Safety prioritized over 42 min extra distance.";
                stage = "REROUTED";

                logs.add("Route comparison completed:");
                logs.add("❌ Corridor A (NH-6): Risk 84% (High Landslide Vulnerability)");
                logs.add("✅ Corridor B (NH-27 / NH-54): Risk 22% (Safe All-Weather Highway, +42 min ETA)");
                logs.add("Decision: Safety override applied for CRITICAL medical supplies");
                break;

            case 12:
            case 13:
            case 14:
            default:
                title = "Step 12-14: Vehicle Diverted on Safe Corridor & Safely Delivered";
                desc = "Reefer truck safely traversed the Haflong bypass, avoiding the blockage entirely, and arrived safely at Silchar Medical College.";
                stage = "DELIVERED";

                vehicleService.updateLocation(demoVehicleId != null ? demoVehicleId : 1L, LocationUpdateRequest.builder()
                        .latitude(24.8333)
                        .longitude(92.7789)
                        .speed(0.0)
                        .timestamp(LocalDateTime.now())
                        .build());

                if (demoShipmentId != null) {
                    shipmentService.updateStatus(demoShipmentId, ShipmentStatus.DELIVERED);
                }

                Alert deliveredAlert = Alert.builder()
                        .type(AlertType.INFO)
                        .severity(Severity.INFO)
                        .title("Shipment Delivered: NER-EMERGENCY-MED-901")
                        .message("Emergency medical vaccines successfully delivered intact to Silchar Medical College via Disaster Bypass.")
                        .latitude(24.8333)
                        .longitude(92.7789)
                        .acknowledged(false)
                        .createdAt(LocalDateTime.now())
                        .build();
                alertRepository.save(deliveredAlert);

                logs.add("Vehicle position updated to Silchar Medical Hub");
                logs.add("Cold-chain vaccine integrity preserved at 4°C");
                logs.add("Mission Status: SUCCESSFUL DISASTER MITIGATION");
                break;
        }

        ShipmentResponse shipResp = null;
        if (demoShipmentId != null) {
            try { shipResp = shipmentService.getShipmentById(demoShipmentId); } catch (Exception ignored) {}
        }
        if (shipResp == null && !shipmentService.getAllShipments().isEmpty()) {
            shipResp = shipmentService.getAllShipments().get(0);
        }

        VehicleResponse vehResp = null;
        if (demoVehicleId != null) {
            try { vehResp = vehicleService.getVehicleById(demoVehicleId); } catch (Exception ignored) {}
        }
        if (vehResp == null && !vehicleService.getAllVehicles().isEmpty()) {
            vehResp = vehicleService.getAllVehicles().get(0);
        }

        RouteResponseDto recRoute = (cachedDemoRoutes != null) ? cachedDemoRoutes.getRecommendedRoute() : null;
        RouteResponseDto altRoute = (cachedDemoRoutes != null && !cachedDemoRoutes.getAlternativeRoutes().isEmpty())
                ? cachedDemoRoutes.getAlternativeRoutes().get(0) : null;

        Alert latest = alertRepository.findAllByOrderByCreatedAtDesc().stream().findFirst().orElse(null);

        return EmergencyDemoStatusDto.builder()
                .currentStep(stepNumber)
                .totalSteps(14)
                .stepTitle(title)
                .stepDescription(desc)
                .stage(stage)
                .shipment(shipResp)
                .vehicle(vehResp)
                .currentRoute(recRoute)
                .alternateRoute(altRoute)
                .currentRiskScore(stepNumber >= 6 && stepNumber <= 11 ? 0.84 : 0.22)
                .latestAlert(latest)
                .logs(logs)
                .build();
    }

    @Transactional
    public void triggerScenario(SimulationTriggerRequest request) {
        String scen = request.getScenario();
        if ("HEAVY_RAIN".equalsIgnoreCase(scen)) {
            HazardEvent h = HazardEvent.builder()
                    .type(HazardType.HEAVY_RAIN)
                    .latitude(25.5788)
                    .longitude(91.8933)
                    .severity(Severity.HIGH)
                    .description("Simulated: Torrential monsoon cloudburst (110 mm/24h) active over Meghalaya mountain corridor.")
                    .source("SIMULATION ENGINE")
                    .status("ACTIVE")
                    .occurredAt(LocalDateTime.now())
                    .expiresAt(LocalDateTime.now().plusHours(12))
                    .build();
            hazardEventRepository.save(h);

            Alert a = Alert.builder()
                    .type(AlertType.WEATHER_WARNING)
                    .severity(Severity.HIGH)
                    .title("Simulated Weather Alert: Heavy Rainfall")
                    .message("Excessive precipitation on NH-6 increasing slope landslide coefficient.")
                    .latitude(25.5788)
                    .longitude(91.8933)
                    .acknowledged(false)
                    .createdAt(LocalDateTime.now())
                    .build();
            alertRepository.save(a);
        } else if ("LANDSLIDE_BLOCKAGE".equalsIgnoreCase(scen)) {
            RoadReport r = RoadReport.builder()
                    .reporter("Highway Patrol Simulator")
                    .latitude(25.1200)
                    .longitude(92.3800)
                    .type(ReportType.LANDSLIDE)
                    .severity(Severity.CRITICAL)
                    .status(ReportStatus.VERIFIED)
                    .description("Simulated: 40-meter section of NH-6 blocked by rockfall at Sonapur.")
                    .photoUrl("https://images.unsplash.com/photo-1547683905-f686c993aae5?auto=format&fit=crop&w=600&q=80")
                    .createdAt(LocalDateTime.now())
                    .verifiedAt(LocalDateTime.now())
                    .build();
            roadReportRepository.save(r);

            Alert a = Alert.builder()
                    .type(AlertType.ROAD_CLOSURE)
                    .severity(Severity.CRITICAL)
                    .title("Simulated Hazard: NH-6 Sonapur Blocked")
                    .message("Road blocked by major landslide. System recommending northern bypass via Lumding.")
                    .latitude(25.1200)
                    .longitude(92.3800)
                    .acknowledged(false)
                    .createdAt(LocalDateTime.now())
                    .build();
            alertRepository.save(a);
        }
    }
}
