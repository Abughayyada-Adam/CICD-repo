package com.swer313.hotel.monolith.availability.dto;

import java.math.BigDecimal;

/**
 * Response DTO that tells the client whether a room is available
 * and what the total price for the stay would be.
 */
public class AvailabilityResponseDto {

    private boolean available;
    private int nights;
    private BigDecimal totalPrice;
    private BigDecimal pricePerNightAverage;
    private String currency;

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getNights() {
        return nights;
    }

    public void setNights(int nights) {
        this.nights = nights;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getPricePerNightAverage() {
        return pricePerNightAverage;
    }

    public void setPricePerNightAverage(BigDecimal pricePerNightAverage) {
        this.pricePerNightAverage = pricePerNightAverage;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}

