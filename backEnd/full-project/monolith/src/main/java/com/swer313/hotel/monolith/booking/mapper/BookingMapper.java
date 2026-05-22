package com.swer313.hotel.monolith.booking.mapper;

import com.swer313.hotel.monolith.booking.dto.BookingDto;
import com.swer313.hotel.monolith.booking.dto.BookingSummaryDto;
import com.swer313.hotel.monolith.booking.model.Booking;

/**
 * Mapper that transforms Booking entities into DTOs.
 */
public class BookingMapper {

    private BookingMapper() {
    }

    public static BookingDto toDto(Booking entity) {
        BookingDto dto = new BookingDto();
        dto.setId(entity.getId());
        dto.setHotelId(entity.getHotel().getId());
        dto.setRoomTypeId(entity.getRoomType().getId());
        dto.setHotelName(entity.getHotel().getName());
        dto.setRoomTypeName(entity.getRoomType().getName());

        dto.setGuestName(entity.getGuestName());
        dto.setGuestEmail(entity.getGuestEmail());
        dto.setGuestPhone(entity.getGuestPhone());

        dto.setCheckInDate(entity.getCheckInDate());
        dto.setCheckOutDate(entity.getCheckOutDate());
        dto.setGuestsCount(entity.getGuestsCount());

        dto.setTotalPrice(entity.getTotalPrice());
        dto.setCurrency("USD");
        dto.setStatus(entity.getStatus());

        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setCancelledAt(entity.getCancelledAt());
        dto.setCancellationReason(entity.getCancellationReason());
        return dto;
    }

    public static BookingSummaryDto toSummary(Booking entity) {
        BookingSummaryDto dto = new BookingSummaryDto();
        dto.setId(entity.getId());
        dto.setHotelName(entity.getHotel().getName());
        dto.setRoomTypeName(entity.getRoomType().getName());
        dto.setCheckInDate(entity.getCheckInDate());
        dto.setCheckOutDate(entity.getCheckOutDate());
        dto.setTotalPrice(entity.getTotalPrice());
        dto.setStatus(entity.getStatus());
        return dto;
    }
}

