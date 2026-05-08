package com.swer313.hotel.monolith.payment.controller;

import com.swer313.hotel.monolith.payment.dto.PaymentDto;
import com.swer313.hotel.monolith.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API for interacting with mocked payments.
 */
@RestController
@RequestMapping("/api/payments")
@Tag(name = "Payment", description = "Mock payment intents and outcomes")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/intent/{bookingId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a payment intent for a booking")
    public PaymentDto createIntent(@PathVariable Long bookingId) {
        return paymentService.createPaymentIntent(bookingId);
    }

    @PostMapping("/{id}/success")
    @Operation(summary = "Simulate successful payment for an intent")
    public PaymentDto markSuccess(@PathVariable Long id) {
        return paymentService.markSuccess(id);
    }

    @PostMapping("/{id}/failure")
    @Operation(summary = "Simulate failed payment for an intent")
    public PaymentDto markFailure(@PathVariable Long id) {
        return paymentService.markFailure(id);
    }

    @PostMapping("/{id}/refund")
    @Operation(summary = "Simulate a refund for a successful payment")
    public PaymentDto refund(@PathVariable Long id) {
        return paymentService.refund(id);
    }

    @GetMapping("/by-booking/{bookingId}")
    @Operation(summary = "List all payment attempts for a booking")
    public List<PaymentDto> listForBooking(@PathVariable Long bookingId) {
        return paymentService.listPaymentsForBooking(bookingId);
    }
}

