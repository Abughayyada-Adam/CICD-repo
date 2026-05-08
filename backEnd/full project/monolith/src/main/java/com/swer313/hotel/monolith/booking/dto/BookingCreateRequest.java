package com.swer313.hotel.monolith.booking.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

/**
 * DTO representing the data a guest sends when creating a new booking.
 */
public class BookingCreateRequest {

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

    @NotBlank(message = "guestName is required")
    private String guestName;

    @NotBlank(message = "guestEmail is required")
    @Email(message = "guestEmail must be a valid email")
    private String guestEmail;

    private String guestPhone;

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

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getGuestEmail() {
        return guestEmail;
    }

    public void setGuestEmail(String guestEmail) {
        this.guestEmail = guestEmail;
    }

    public String getGuestPhone() {
        return guestPhone;
    }

    public void setGuestPhone(String guestPhone) {
        this.guestPhone = guestPhone;
    }
}

