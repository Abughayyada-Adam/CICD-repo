package com.swer313.hotel.monolith.booking.model;

/**
 * Enumeration for the lifecycle of a booking.
 */
public enum BookingStatus {
    /**
     * Booking was created but payment has not been confirmed yet.
     */
    PENDING,

    /**
     * Booking is fully confirmed and should block availability.
     */
    CONFIRMED,

    /**
     * Booking was cancelled according to the cancellation policy.
     */
    CANCELLED
}

