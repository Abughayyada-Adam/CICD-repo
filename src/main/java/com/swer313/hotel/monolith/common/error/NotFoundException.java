package com.swer313.hotel.monolith.common.error;

/**
 * Simple domain‑level exception used when a requested resource cannot be found.
 * Throwing a dedicated exception helps the global error handler produce consistent responses.
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}

