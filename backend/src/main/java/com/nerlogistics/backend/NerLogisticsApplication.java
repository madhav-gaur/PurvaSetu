package com.nerlogistics.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Entry Point for North Eastern Region (NER)
 * AI Smart Logistics & Accessibility Intelligence Platform.
 */
@SpringBootApplication
public class NerLogisticsApplication {

    public static void main(String[] args) {
        SpringApplication.run(NerLogisticsApplication.class, args);
        System.out.println("=========================================================");
        System.out.println("🚀 NER AI Smart Logistics Platform Backend Started!");
        System.out.println("📡 Server URL: http://localhost:8080");
        System.out.println("📄 Swagger UI: http://localhost:8080/swagger-ui.html");
        System.out.println("=========================================================");
    }
}
