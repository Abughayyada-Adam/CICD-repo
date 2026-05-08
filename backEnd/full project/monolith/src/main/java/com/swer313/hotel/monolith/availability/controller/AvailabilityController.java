package com.swer313.hotel.monolith.availability.controller;

import com.swer313.hotel.monolith.availability.dto.AvailabilityRequestDto;
import com.swer313.hotel.monolith.availability.dto.AvailabilityResponseDto;
import com.swer313.hotel.monolith.availability.service.AvailabilityPricingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST endpoint for availability and pricing checks.
 */
@RestController
@RequestMapping("/api/availability")
@Tag(name = "Availability & Pricing", description = "Check availability and dynamic pricing")
public class AvailabilityController {

    private final AvailabilityPricingService availabilityPricingService;

    public AvailabilityController(AvailabilityPricingService availabilityPricingService) {
        this.availabilityPricingService = availabilityPricingService;
    }

    @PostMapping("/check")
    @Operation(summary = "Check if a room type is available for a date range and compute price")
    public AvailabilityResponseDto check(@Valid @RequestBody AvailabilityRequestDto request) {
        return availabilityPricingService.checkAvailabilityAndPrice(request);
    }
}

