package com.swer313.hotel.monolith;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the modular monolith application.
 * The OpenAPIDefinition annotation configures the Swagger UI metadata.
 */
@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Tourism Hotel Booking API (Monolith)",
                version = "1.0.0",
                description = "Backend for hotel catalog, availability, booking, payment, and notifications"
        )
)
public class MonolithApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonolithApplication.class, args);
    }
}

