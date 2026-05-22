package com.swer313.hotel.monolith.payment.model;

/**
 * Status for a payment attempt related to a booking.
 */
public enum PaymentStatus {
    PENDING,
    SUCCESS,
    FAILED,
    REFUNDED
}

