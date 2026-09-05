package com.nerlogistics.backend.config;

import com.nerlogistics.backend.entity.*;
import com.nerlogistics.backend.enums.*;
import com.nerlogistics.backend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final VehicleLocationRepository vehicleLocationRepository;
    private final ShipmentRepository shipmentRepository;
    private final RoadRepository roadRepository;
    private final RoadSegmentRepository roadSegmentRepository;
    private final RoadReportRepository roadReportRepository;
    private final RoadAdvisoryRepository roadAdvisoryRepository;
    private final HazardEventRepository hazardEventRepository;
    private final AlertRepository alertRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        log.info("Checking and seeding realistic NER Logistics demo dataset...");

        // 1. Seed Users
        if (userRepository.count() == 0) {
            userRepository.saveAll(List.of(
                    User.builder()
                            .name("NER Logistics Administrator")
                            .email("admin@nerlogistics.in")
                            .password(passwordEncoder.encode("Admin@123"))
                            .phone("+91 98640 12345")
                            .role(Role.ADMIN)
                            .enabled(true)
                            .build(),
                    User.builder()
                            .name("Ananya Sharma (Command Center Operator)")
                            .email("operator@nerlogistics.in")
                            .password(passwordEncoder.encode("Operator@123"))
                            .phone("+91 98640 54321")
                            .role(Role.OPERATOR)
                            .enabled(true)
                            .build(),
                    User.builder()
                            .name("Manash Kalita (Senior Hill Driver)")
                            .email("driver@nerlogistics.in")
                            .password(passwordEncoder.encode("Driver@123"))
                            .phone("+91 94350 11223")
                            .role(Role.DRIVER)
                            .enabled(true)
                            .build(),
                    User.builder()
                            .name("Tashi Norbu (Field Inspector)")
                            .email("agent@nerlogistics.in")
                            .password(passwordEncoder.encode("Agent@123"))
                            .phone("+91 94360 99887")
                            .role(Role.FIELD_AGENT)
                            .enabled(true)
                            .build()
            ));
            log.info("Seeded default users.");
        }

        // 2. Seed Vehicles
        if (vehicleRepository.count() == 0) {
            Vehicle v1 = vehicleRepository.save(Vehicle.builder()
                    .vehicleNumber("AS-01-GC-4412")
                    .vehicleType("Ashok Leyland 1616 Cold-Chain Reefer")
                    .driver("Manash Kalita")
                    .driverPhone("+91 94350 11223")
                    .currentLatitude(26.1445)
                    .currentLongitude(91.7362)
                    .speed(54.0)
                    .status(VehicleStatus.IN_TRANSIT)
                    .currentShipment("NER-EMERGENCY-MED-901")
                    .lastUpdated(LocalDateTime.now())
                    .build());

            Vehicle v2 = vehicleRepository.save(Vehicle.builder()
                    .vehicleNumber("AS-01-EC-9081")
                    .vehicleType("Tata 407 Heavy Truck")
                    .driver("Bipul Das")
                    .driverPhone("+91 98640 22334")
                    .currentLatitude(26.7509)
                    .currentLongitude(94.2037)
                    .speed(0.0)
                    .status(VehicleStatus.AVAILABLE)
                    .lastUpdated(LocalDateTime.now())
                    .build());

            Vehicle v3 = vehicleRepository.save(Vehicle.builder()
                    .vehicleNumber("ML-05-AB-3341")
                    .vehicleType("Mahindra Bolero Maxi Truck")
                    .driver("Kenny Lyngdoh")
                    .driverPhone("+91 98560 44556")
                    .currentLatitude(25.5788)
                    .currentLongitude(91.8933)
                    .speed(42.0)
                    .status(VehicleStatus.IN_TRANSIT)
                    .currentShipment("NER-RELIEF-GRAIN-108")
                    .lastUpdated(LocalDateTime.now())
                    .build());

            Vehicle v4 = vehicleRepository.save(Vehicle.builder()
                    .vehicleNumber("AR-01-TR-8812")
                    .vehicleType("BharatBenz 1217C 4x4 Hill Hauler")
                    .driver("Tashi Wangchu")
                    .driverPhone("+91 94360 77889")
                    .currentLatitude(28.1400)
                    .currentLongitude(95.8300)
                    .speed(35.0)
                    .status(VehicleStatus.IN_TRANSIT)
                    .lastUpdated(LocalDateTime.now())
                    .build());

            Vehicle v5 = vehicleRepository.save(Vehicle.builder()
                    .vehicleNumber("WB-74-J-9901")
                    .vehicleType("Eicher Pro 2049 Hill Truck")
                    .driver("Pemba Sherpa")
                    .driverPhone("+91 97330 44551")
                    .currentLatitude(27.0410)
                    .currentLongitude(88.2663)
                    .speed(32.0)
                    .status(VehicleStatus.IN_TRANSIT)
                    .currentShipment("NER-RELIEF-ANJAW-303")
                    .lastUpdated(LocalDateTime.now())
                    .build());

            // Add Location Breadcrumbs
            vehicleLocationRepository.saveAll(List.of(
                    VehicleLocation.builder().vehicleId(v1.getId()).latitude(26.1445).longitude(91.7362).speed(0.0).timestamp(LocalDateTime.now().minusHours(2)).build(),
                    VehicleLocation.builder().vehicleId(v1.getId()).latitude(26.0120).longitude(91.7900).speed(48.0).timestamp(LocalDateTime.now().minusHours(1)).build(),
                    VehicleLocation.builder().vehicleId(v1.getId()).latitude(25.9200).longitude(91.8350).speed(54.0).timestamp(LocalDateTime.now()).build()
            ));

            log.info("Seeded vehicle fleet.");
        }

        // 3. Seed Roads and Segments
        if (roadRepository.count() == 0) {
            Road r1 = roadRepository.save(Road.builder()
                    .name("NH-6 (Guwahati - Shillong - Silchar Corridor)")
                    .roadNumber("NH-6")
                    .state("Meghalaya / Assam")
                    .status(RoadStatus.PARTIAL)
                    .build());

            Road r2 = roadRepository.save(Road.builder()
                    .name("NH-27 (East-West Highway Bypass via Lumding)")
                    .roadNumber("NH-27")
                    .state("Assam")
                    .status(RoadStatus.OPEN)
                    .build());

            Road r3 = roadRepository.save(Road.builder()
                    .name("NH-29 (Dimapur - Kohima Highway)")
                    .roadNumber("NH-29")
                    .state("Nagaland")
                    .status(RoadStatus.OPEN)
                    .build());

            roadSegmentRepository.saveAll(List.of(
                    RoadSegment.builder().road(r1).startLatitude(25.5788).startLongitude(91.8933).endLatitude(25.1200).endLongitude(92.3800).length(68.0).status(RoadStatus.PARTIAL).riskScore(0.82).accessibilityScore(0.18).build(),
                    RoadSegment.builder().road(r2).startLatitude(26.1445).startLongitude(91.7362).endLatitude(25.7500).endLongitude(93.1700).length(142.0).status(RoadStatus.OPEN).riskScore(0.22).accessibilityScore(0.78).build(),
                    RoadSegment.builder().road(r3).startLatitude(25.9068).startLongitude(93.7271).endLatitude(25.6701).endLongitude(94.1077).length(74.0).status(RoadStatus.OPEN).riskScore(0.38).accessibilityScore(0.62).build()
            ));

            log.info("Seeded roads and corridors.");
        }

        // 4. Seed Shipments
        if (shipmentRepository.count() == 0) {
            Vehicle v1 = vehicleRepository.findAll().stream().findFirst().orElse(null);

            shipmentRepository.saveAll(List.of(
                    Shipment.builder()
                            .trackingNumber("NER-EMERGENCY-MED-901")
                            .source("Guwahati Logistics Hub, Assam")
                            .destination("Silchar Medical Hub, Assam")
                            .priority(ShipmentPriority.CRITICAL)
                            .status(ShipmentStatus.IN_TRANSIT)
                            .cargoType("Emergency Vaccines & Critical Life-Saving Medical Rations")
                            .weight(3.5)
                            .vehicle(v1)
                            .expectedDelivery(LocalDateTime.now().plusHours(6))
                            .build(),
                    Shipment.builder()
                            .trackingNumber("NER-AGRI-TEA-402")
                            .source("Jorhat Tea Estate Hub, Assam")
                            .destination("Guwahati Inland Port, Assam")
                            .priority(ShipmentPriority.HIGH)
                            .status(ShipmentStatus.ASSIGNED)
                            .cargoType("Export Grade Organic CTC & Orthodox Tea")
                            .weight(14.0)
                            .expectedDelivery(LocalDateTime.now().plusHours(10))
                            .build(),
                    Shipment.builder()
                            .trackingNumber("NER-RELIEF-GRAIN-108")
                            .source("Guwahati Central Warehouse, Assam")
                            .destination("Dibang Valley Relief Post, Arunachal Pradesh")
                            .priority(ShipmentPriority.HIGH)
                            .status(ShipmentStatus.IN_TRANSIT)
                            .cargoType("Non-Perishable Fortified Grains & Water Purification Kits")
                            .weight(8.2)
                            .expectedDelivery(LocalDateTime.now().plusHours(18))
                            .build(),
                    Shipment.builder()
                            .trackingNumber("NER-CARGO-IND-550")
                            .source("Guwahati Industrial Area, Assam")
                            .destination("Agartala Smart City Hub, Tripura")
                            .priority(ShipmentPriority.MEDIUM)
                            .status(ShipmentStatus.CREATED)
                            .cargoType("Solar Photovoltaic Cells & Telecom Hardware")
                            .weight(6.8)
                            .expectedDelivery(LocalDateTime.now().plusHours(24))
                            .build(),
                    Shipment.builder()
                            .trackingNumber("NER-RELIEF-ANJAW-303")
                            .source("Darjeeling Gateway Hub, West Bengal")
                            .destination("Anjaw Border Post, Arunachal Pradesh")
                            .priority(ShipmentPriority.HIGH)
                            .status(ShipmentStatus.IN_TRANSIT)
                            .cargoType("Extreme Altitude Medical Kits & Heating Fuel")
                            .weight(4.2)
                            .expectedDelivery(LocalDateTime.now().plusHours(14))
                            .build()
            ));

            log.info("Seeded initial shipments.");
        }

        // 5. Seed Road Reports
        if (roadReportRepository.count() == 0) {
            roadReportRepository.saveAll(List.of(
                    RoadReport.builder()
                            .reporter("Officer Baruah (BRO Highway Patrol)")
                            .latitude(25.1200)
                            .longitude(92.3800)
                            .type(ReportType.LANDSLIDE)
                            .description("Severe mudslide blocking two lanes on NH-6 near Sonapur Tunnel. Heavy machinery clearing debris.")
                            .severity(Severity.CRITICAL)
                            .status(ReportStatus.VERIFIED)
                            .photoUrl("https://images.unsplash.com/photo-1547683905-f686c993aae5?auto=format&fit=crop&w=600&q=80")
                            .verifiedAt(LocalDateTime.now().minusMinutes(40))
                            .build(),
                    RoadReport.builder()
                            .reporter("Driver Kenny Lyngdoh")
                            .latitude(25.5788)
                            .longitude(91.8933)
                            .type(ReportType.HEAVY_TRAFFIC)
                            .description("Slow-moving traffic due to heavy rainfall and dense mountain fog on Shillong bypass.")
                            .severity(Severity.MEDIUM)
                            .status(ReportStatus.VERIFIED)
                            .verifiedAt(LocalDateTime.now().minusMinutes(20))
                            .build(),
                    RoadReport.builder()
                            .reporter("Field Inspector Tashi")
                            .latitude(28.1400)
                            .longitude(95.8300)
                            .type(ReportType.DAMAGED_ROAD)
                            .description("Road shoulder erosion on Dibang Valley mountain approach.")
                            .severity(Severity.HIGH)
                            .status(ReportStatus.PENDING)
                            .build(),
                    RoadReport.builder()
                            .reporter("Inspector Dorjee (Anjaw Border Police)")
                            .latitude(27.8860)
                            .longitude(96.7970)
                            .type(ReportType.LANDSLIDE)
                            .description("Monsoon slope debris on Hawai-Walong highway in Anjaw. 4x4 vehicles only.")
                            .severity(Severity.HIGH)
                            .status(ReportStatus.VERIFIED)
                            .verifiedAt(LocalDateTime.now().minusHours(1))
                            .build(),
                    RoadReport.builder()
                            .reporter("Hill Patrol Darjeeling")
                            .latitude(27.0410)
                            .longitude(88.2663)
                            .type(ReportType.DAMAGED_ROAD)
                            .description("Road subsidence near Rohini ghat road, Darjeeling foothills.")
                            .severity(Severity.MEDIUM)
                            .status(ReportStatus.VERIFIED)
                            .verifiedAt(LocalDateTime.now().minusHours(3))
                            .build()
            ));
            log.info("Seeded field reports.");
        }

        // 6. Seed Advisories
        if (roadAdvisoryRepository.count() == 0) {
            roadAdvisoryRepository.saveAll(List.of(
                    RoadAdvisory.builder()
                            .title("NH-6 Monsoon Landslide Alert & Heavy Vehicle Diversion")
                            .description("Meghalaya Traffic Police advises heavy freight vehicles bound for Silchar/Tripura to utilize NH-27 Lumding corridor due to slope instability at Sonapur.")
                            .type(AdvisoryType.ROAD_CLOSURE)
                            .severity(Severity.CRITICAL)
                            .source("Meghalaya State Disaster Management Authority (MSDMA)")
                            .sourceUrl("https://msdma.gov.in/advisories")
                            .validFrom(LocalDateTime.now().minusHours(2))
                            .validUntil(LocalDateTime.now().plusDays(2))
                            .status("ACTIVE")
                            .build(),
                    RoadAdvisory.builder()
                            .title("Dense Fog & Low Visibility Advisory across Barail Range")
                            .description("Speeds restricted to 30 km/h due to visibility under 20 meters.")
                            .type(AdvisoryType.WEATHER_ADVISORY)
                            .severity(Severity.MEDIUM)
                            .source("IMD Regional Meteorological Centre, Guwahati")
                            .validFrom(LocalDateTime.now().minusHours(6))
                            .validUntil(LocalDateTime.now().plusHours(18))
                            .status("ACTIVE")
                            .build()
            ));
            log.info("Seeded official advisories.");
        }

        // 7. Seed Hazards & Alerts
        if (hazardEventRepository.count() == 0) {
            hazardEventRepository.saveAll(List.of(
                    HazardEvent.builder()
                            .type(HazardType.LANDSLIDE)
                            .latitude(25.1200)
                            .longitude(92.3800)
                            .severity(Severity.CRITICAL)
                            .description("Active Landslide on NH-6 Ghat Section")
                            .source("NDMA SACHET / IMD")
                            .status("ACTIVE")
                            .occurredAt(LocalDateTime.now().minusHours(1))
                            .expiresAt(LocalDateTime.now().plusHours(24))
                            .build(),
                    HazardEvent.builder()
                            .type(HazardType.HEAVY_RAIN)
                            .latitude(25.5788)
                            .longitude(91.8933)
                            .severity(Severity.HIGH)
                            .description("94mm/24h High Accumulation Precipitation Alert")
                            .source("IMD Guwahati")
                            .status("ACTIVE")
                            .occurredAt(LocalDateTime.now().minusHours(3))
                            .expiresAt(LocalDateTime.now().plusHours(12))
                            .build()
            ));
        }

        if (alertRepository.count() == 0) {
            alertRepository.saveAll(List.of(
                    Alert.builder()
                            .type(AlertType.LANDSLIDE_WARNING)
                            .severity(Severity.CRITICAL)
                            .title("CRITICAL: Active Landslide Obstruction on NH-6")
                            .message("Severe debris flow reported near Sonapur Tunnel. Medicine Shipment NER-EMERGENCY-MED-901 requires immediate rerouting.")
                            .latitude(25.1200)
                            .longitude(92.3800)
                            .acknowledged(false)
                            .createdAt(LocalDateTime.now().minusMinutes(15))
                            .build(),
                    Alert.builder()
                            .type(AlertType.WEATHER_WARNING)
                            .severity(Severity.HIGH)
                            .title("WEATHER ALERT: IMD Red Warning for Meghalaya Plateau")
                            .message("Extreme rain saturation increasing slope instability.")
                            .latitude(25.5788)
                            .longitude(91.8933)
                            .acknowledged(false)
                            .createdAt(LocalDateTime.now().minusHours(1))
                            .build(),
                    Alert.builder()
                            .type(AlertType.REROUTE)
                            .severity(Severity.HIGH)
                            .title("Safer Bypass Route Available (78% Lower Risk)")
                            .message("AI Pathfinding recommends diverting via NH-27 Lumding-Haflong corridor.")
                            .latitude(26.1445)
                            .longitude(91.7362)
                            .acknowledged(false)
                            .createdAt(LocalDateTime.now().minusMinutes(5))
                            .build()
            ));
            log.info("Seeded operational alerts.");
        }

        log.info("✅ All seed data loaded successfully.");
    }
}
