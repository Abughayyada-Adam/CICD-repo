package com.swer313.hotel.monolith.booking.service;

import com.swer313.hotel.monolith.availability.dto.AvailabilityRequestDto;
import com.swer313.hotel.monolith.availability.dto.AvailabilityResponseDto;
import com.swer313.hotel.monolith.availability.service.AvailabilityPricingService;
import com.swer313.hotel.monolith.booking.dto.BookingCreateRequest;
import com.swer313.hotel.monolith.booking.dto.BookingDto;
import com.swer313.hotel.monolith.booking.dto.BookingSummaryDto;
import com.swer313.hotel.monolith.booking.mapper.BookingMapper;
import com.swer313.hotel.monolith.booking.model.Booking;
import com.swer313.hotel.monolith.booking.model.BookingStatus;
import com.swer313.hotel.monolith.booking.repository.BookingRepository;
import com.swer313.hotel.monolith.catalog.model.RoomType;
import com.swer313.hotel.monolith.catalog.service.RoomTypeService;
import com.swer313.hotel.monolith.common.error.BusinessException;
import com.swer313.hotel.monolith.common.error.NotFoundException;
import com.swer313.hotel.monolith.notification.service.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * BookingService implements the main booking lifecycle:
 * - create booking (PENDING)
 * - confirm booking (called from PaymentService)
 * - cancel booking respecting a simple cancellation policy
 * - history listing for guests and managers
 */
@Service
@Transactional
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomTypeService roomTypeService;
    private final AvailabilityPricingService availabilityPricingService;
    private final NotificationService notificationService;

    public BookingService(BookingRepository bookingRepository,
                          RoomTypeService roomTypeService,
                          AvailabilityPricingService availabilityPricingService,
                          NotificationService notificationService) {
        this.bookingRepository = bookingRepository;
        this.roomTypeService = roomTypeService;
        this.availabilityPricingService = availabilityPricingService;
        this.notificationService = notificationService;
    }

    /**
     * Creates a booking with status PENDING after verifying availability and calculating price.
     */
    public BookingDto createBooking(BookingCreateRequest request) {
        // 0. Validate dates
        if (request.getCheckInDate().isBefore(LocalDate.now())) {
            throw new BusinessException("Check-in date cannot be in the past");
        }
        if (request.getCheckOutDate().isBefore(request.getCheckInDate().plusDays(1))) {
            throw new BusinessException("Check-out date must be at least one day after check-in");
        }

        RoomType roomType = roomTypeService.getRoomTypeEntity(request.getRoomTypeId());

        // 1. Prevent duplicate booking by same guest
        long duplicateCount = bookingRepository.countExistingByGuestAndDates(
                request.getRoomTypeId(),
                request.getGuestEmail(),
                List.of(BookingStatus.PENDING, BookingStatus.CONFIRMED),
                request.getCheckInDate(),
                request.getCheckOutDate()
        );
        if (duplicateCount > 0) {
            throw new BusinessException("You already have a booking for this room type on the selected dates");
        }

        // 2. Check availability and price
        AvailabilityRequestDto availabilityRequest = new AvailabilityRequestDto();
        availabilityRequest.setRoomTypeId(request.getRoomTypeId());
        availabilityRequest.setCheckInDate(request.getCheckInDate());
        availabilityRequest.setCheckOutDate(request.getCheckOutDate());
        availabilityRequest.setGuestsCount(request.getGuestsCount());

        AvailabilityResponseDto availability = availabilityPricingService.checkAvailabilityAndPrice(availabilityRequest);
        if (!availability.isAvailable()) {
            throw new BusinessException("No availability for the chosen dates");
        }

        // 3. Create booking
        Booking booking = new Booking();
        booking.setHotel(roomType.getHotel());
        booking.setRoomType(roomType);
        booking.setGuestName(request.getGuestName());
        booking.setGuestEmail(request.getGuestEmail());
        booking.setGuestPhone(request.getGuestPhone());
        booking.setCheckInDate(request.getCheckInDate());
        booking.setCheckOutDate(request.getCheckOutDate());
        booking.setGuestsCount(request.getGuestsCount());
        booking.setTotalPrice(availability.getTotalPrice());
        booking.setStatus(BookingStatus.CONFIRMED); // Changed from PENDING to CONFIRMED
        booking.setCreatedAt(OffsetDateTime.now());
        booking.setUpdatedAt(booking.getCreatedAt());

        Booking saved = bookingRepository.save(booking);
        
        // Send confirmation email immediately
        notificationService.sendBookingConfirmation(saved);
        
        return BookingMapper.toDto(saved);
    }

    /**
     * Confirm a booking after a successful payment.
     * This is intentionally idempotent: confirming an already confirmed booking is a no‑op.
     */
    public BookingDto confirmBooking(Long bookingId) {
        Booking booking = getBookingEntity(bookingId);
        if (booking.getStatus() == BookingStatus.CONFIRMED) {
            return BookingMapper.toDto(booking);
        }
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BusinessException("Cannot confirm a cancelled booking");
        }
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setUpdatedAt(OffsetDateTime.now());
        notificationService.sendBookingConfirmation(booking);
        return BookingMapper.toDto(booking);
    }

    /**
     * Simple cancellation policy:
     * - free cancellation if more than 2 days before check‑in
     * - otherwise cancellation is rejected
     */
    public BookingDto cancelBooking(Long bookingId, String reason) {
        Booking booking = getBookingEntity(bookingId);
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            return BookingMapper.toDto(booking);
        }

        LocalDate today = LocalDate.now();
        long daysBeforeCheckIn = ChronoUnit.DAYS.between(today, booking.getCheckInDate());
        if (daysBeforeCheckIn <= 2) {
            throw new BusinessException("Cancellation not allowed within 2 days of check‑in");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCancelledAt(OffsetDateTime.now());
        booking.setCancellationReason(reason);
        booking.setUpdatedAt(booking.getCancelledAt());
        notificationService.sendBookingCancellation(booking);
        return BookingMapper.toDto(booking);
    }

    public BookingDto getBooking(Long id) {
        return BookingMapper.toDto(getBookingEntity(id));
    }

    public List<BookingSummaryDto> listBookingsForGuest(String guestEmail) {
        return bookingRepository.findByGuestEmailOrderByCheckInDateDesc(guestEmail)
                .stream()
                .map(BookingMapper::toSummary)
                .collect(Collectors.toList());
    }

    public List<BookingDto> listAllBookings(BookingStatus status) {
        List<Booking> bookings =
                status == null
                        ? bookingRepository.findAllByOrderByCreatedAtDesc()
                        : bookingRepository.findByStatusOrderByCreatedAtDesc(status);
        return bookings.stream().map(BookingMapper::toDto).toList();
    }

    public Booking getBookingEntity(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking with id " + id + " not found"));
    }
}

