package com.swer313.hotel.monolith.availability.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

/**
 * Request DTO for checking room availability and pricing.
 */
public class AvailabilityRequestDto {

    @NotNull(message = "roomTypeId is required")
    private Long roomTypeId;

    @NotNull(message = "checkInDate is required")
    @Future(message = "checkInDate must be in the future")
    private LocalDate checkInDate;

    @NotNull(message = "checkOutDate is required")
    @Future(message = "checkOutDate must be in the future")
    private LocalDate checkOutDate;

    @Positive(message = "guestsCount must be greater than 0")
    private int guestsCount;

    public Long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(Long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public int getGuestsCount() {
        return guestsCount;
    }

    public void setGuestsCount(int guestsCount) {
        this.guestsCount = guestsCount;
    }
}

