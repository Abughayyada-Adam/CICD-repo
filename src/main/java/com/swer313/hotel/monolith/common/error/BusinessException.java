package com.swer313.hotel.monolith.common.error;

/**
 * BusinessException is thrown when a domain rule is violated
 * (for example, booking when no rooms are available or violating cancellation policy).
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}

