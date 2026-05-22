package com.swer313.hotel.monolith.booking.controller;

import com.swer313.hotel.monolith.booking.dto.BookingCreateRequest;
import com.swer313.hotel.monolith.booking.dto.BookingDto;
import com.swer313.hotel.monolith.booking.dto.BookingSummaryDto;
import com.swer313.hotel.monolith.booking.model.BookingStatus;
import com.swer313.hotel.monolith.booking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API for creating, cancelling, and listing bookings.
 */
@RestController
@RequestMapping("/api/bookings")
@Tag(name = "Booking", description = "Create and manage bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new booking (status = PENDING)")
    public BookingDto createBooking(@Valid @RequestBody BookingCreateRequest request) {
        return bookingService.createBooking(request);
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel a booking if allowed by cancellation policy")
    public BookingDto cancel(@PathVariable Long id,
                             @RequestParam(name = "reason", defaultValue = "Guest requested cancellation") String reason) {
        return bookingService.cancelBooking(id, reason);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get booking details")
    public BookingDto get(@PathVariable Long id) {
        return bookingService.getBooking(id);
    }

    @GetMapping("/guest/{email}")
    @Operation(summary = "Get booking history for a guest by email")
    public List<BookingSummaryDto> history(@PathVariable String email) {
        return bookingService.listBookingsForGuest(email);
    }

    @GetMapping
    @Operation(summary = "List all bookings (optionally filtered by status)")
    public List<BookingDto> listAll(@RequestParam(name = "status", required = false) BookingStatus status) {
        return bookingService.listAllBookings(status);
    }

    @PostMapping("/{id}/confirm")
    @Operation(summary = "Confirm a pending booking")
    public BookingDto confirm(@PathVariable Long id) {
        return bookingService.confirmBooking(id);
    }
}

