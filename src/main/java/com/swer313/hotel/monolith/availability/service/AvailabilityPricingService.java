package com.swer313.hotel.monolith.availability.service;

import com.swer313.hotel.monolith.availability.dto.AvailabilityRequestDto;
import com.swer313.hotel.monolith.availability.dto.AvailabilityResponseDto;
import com.swer313.hotel.monolith.booking.model.Booking;
import com.swer313.hotel.monolith.booking.model.BookingStatus;
import com.swer313.hotel.monolith.booking.repository.BookingRepository;
import com.swer313.hotel.monolith.catalog.model.RoomType;
import com.swer313.hotel.monolith.catalog.service.RoomTypeService;
import com.swer313.hotel.monolith.common.error.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.EnumSet;
import java.util.List;

/**
 * AvailabilityPricingService encapsulates both availability checks and
 * simple pricing rules for a room type.
 *
 * For Step 1 we implement:
 * - prevention of double‑booking at monolith level
 * - basic weekday/weekend multiplier pricing
 */
@Service
@Transactional(readOnly = true)
public class AvailabilityPricingService {

    private static final BigDecimal WEEKDAY_MULTIPLIER = BigDecimal.valueOf(1.0);
    private static final BigDecimal WEEKEND_MULTIPLIER = BigDecimal.valueOf(1.2);

    private final RoomTypeService roomTypeService;
    private final BookingRepository bookingRepository;

    public AvailabilityPricingService(RoomTypeService roomTypeService,
                                      BookingRepository bookingRepository) {
        this.roomTypeService = roomTypeService;
        this.bookingRepository = bookingRepository;
    }

    public AvailabilityResponseDto checkAvailabilityAndPrice(AvailabilityRequestDto request) {
        RoomType roomType = roomTypeService.getRoomTypeEntity(request.getRoomTypeId());
        validateDates(request.getCheckInDate(), request.getCheckOutDate());
        validateCapacity(roomType, request.getGuestsCount());

        int nights = (int) ChronoUnit.DAYS.between(request.getCheckInDate(), request.getCheckOutDate());
        if (nights <= 0) {
            throw new BusinessException("checkOutDate must be after checkInDate");
        }

        boolean available = isAvailable(roomType, request.getCheckInDate(), request.getCheckOutDate());

        // Throw exception if no rooms are available
        if (!available) {
            throw new BusinessException("Room type is fully booked for the selected dates");
        }

        AvailabilityResponseDto response = new AvailabilityResponseDto();
        response.setAvailable(true);
        response.setNights(nights);

        BigDecimal totalPrice = calculateTotalPrice(roomType, request.getCheckInDate(), request.getCheckOutDate());
        response.setTotalPrice(totalPrice);
        response.setPricePerNightAverage(totalPrice.divide(BigDecimal.valueOf(nights), 2, RoundingMode.HALF_UP));
        response.setCurrency("USD");
        return response;
    }

    private void validateDates(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null || checkOut == null) {
            throw new BusinessException("Both checkInDate and checkOutDate are required");
        }
        if (!checkOut.isAfter(checkIn)) {
            throw new BusinessException("checkOutDate must be after checkInDate");
        }
    }

    private void validateCapacity(RoomType roomType, int guestsCount) {
        if (guestsCount <= 0) {
            throw new BusinessException("guestsCount must be positive");
        }
        if (guestsCount > roomType.getCapacity()) {
            throw new BusinessException("guestsCount exceeds room capacity");
        }
    }

    /**
     * Double‑booking prevention logic. We count the number of overlapping bookings for this room type
     * and compare it to the total number of physical rooms.
     */
    private boolean isAvailable(RoomType roomType, LocalDate checkIn, LocalDate checkOut) {
        List<Booking> overlapping = bookingRepository.findOverlappingBookings(
                roomType.getId(),
                List.copyOf(EnumSet.of(BookingStatus.PENDING, BookingStatus.CONFIRMED)),
                checkIn,
                checkOut
        );
        System.out.println("Overlapping count: " + overlapping.size());
        return overlapping.size() < roomType.getTotalRooms();
    }
    private BigDecimal calculateTotalPrice(RoomType roomType, LocalDate checkIn, LocalDate checkOut) {
        BigDecimal result = BigDecimal.ZERO;
        LocalDate cursor = checkIn;
        while (cursor.isBefore(checkOut)) {
            DayOfWeek dow = cursor.getDayOfWeek();
            BigDecimal multiplier = isWeekend(dow) ? WEEKEND_MULTIPLIER : WEEKDAY_MULTIPLIER;
            result = result.add(roomType.getBasePricePerNight().multiply(multiplier));
            cursor = cursor.plusDays(1);
        }
        return result.setScale(2, RoundingMode.HALF_UP);
    }

    private boolean isWeekend(DayOfWeek dayOfWeek) {
        return dayOfWeek == DayOfWeek.FRIDAY
                || dayOfWeek == DayOfWeek.SATURDAY
                || dayOfWeek == DayOfWeek.SUNDAY;
    }
}

