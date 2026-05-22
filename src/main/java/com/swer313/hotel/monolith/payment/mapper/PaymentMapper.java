package com.swer313.hotel.monolith.payment.mapper;

import com.swer313.hotel.monolith.payment.dto.PaymentDto;
import com.swer313.hotel.monolith.payment.model.Payment;

/**
 * Mapper to convert Payment entities to DTOs.
 */
public class PaymentMapper {

    private PaymentMapper() {
    }

    public static PaymentDto toDto(Payment entity) {
        PaymentDto dto = new PaymentDto();
        dto.setId(entity.getId());
        dto.setBookingId(entity.getBooking().getId());
        dto.setAmount(entity.getAmount());
        dto.setCurrency(entity.getCurrency());
        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}

