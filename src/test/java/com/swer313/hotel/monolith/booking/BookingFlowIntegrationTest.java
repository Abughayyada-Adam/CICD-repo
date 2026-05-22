package com.swer313.hotel.monolith.booking;

import com.swer313.hotel.monolith.availability.dto.AvailabilityRequestDto;
import com.swer313.hotel.monolith.availability.dto.AvailabilityResponseDto;
import com.swer313.hotel.monolith.availability.service.AvailabilityPricingService;
import com.swer313.hotel.monolith.booking.dto.BookingCreateRequest;
import com.swer313.hotel.monolith.booking.dto.BookingDto;
import com.swer313.hotel.monolith.booking.model.BookingStatus;
import com.swer313.hotel.monolith.booking.service.BookingService;
import com.swer313.hotel.monolith.catalog.dto.HotelCreateUpdateRequest;
import com.swer313.hotel.monolith.catalog.dto.RoomTypeCreateUpdateRequest;
import com.swer313.hotel.monolith.catalog.dto.RoomTypeDto;
import com.swer313.hotel.monolith.catalog.service.HotelService;
import com.swer313.hotel.monolith.catalog.service.RoomTypeService;
import com.swer313.hotel.monolith.payment.dto.PaymentDto;
import com.swer313.hotel.monolith.payment.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * End‑to‑end integration test that exercises the main booking flow:
 * 1) create hotel + room type
 * 2) check availability
 * 3) create booking (PENDING)
 * 4) create payment intent and mark it as successful
 * 5) verify booking is CONFIRMED
 */
@SpringBootTest
@ActiveProfiles("test")
public class BookingFlowIntegrationTest {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private RoomTypeService roomTypeService;

    @Autowired
    private AvailabilityPricingService availabilityPricingService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private PaymentService paymentService;

    @Test
    void endToEndBookingFlowShouldSucceed() {
        // Step 1: set up a hotel and a room type that we can book.
        HotelCreateUpdateRequest hotelReq = new HotelCreateUpdateRequest();
        hotelReq.setName("Integration Hotel");
        hotelReq.setCity("Test City");
        hotelReq.setAddress("123 Test Street");
        hotelReq.setStarRating(4);
        var hotelDto = hotelService.createHotel(hotelReq);

        RoomTypeCreateUpdateRequest roomReq = new RoomTypeCreateUpdateRequest();
        roomReq.setHotelId(hotelDto.getId());
        roomReq.setName("Standard Room");
        roomReq.setCapacity(2);
        roomReq.setTotalRooms(5);
        roomReq.setBasePricePerNight(BigDecimal.valueOf(100));
        roomReq.setAmenities("WiFi,TV");
        RoomTypeDto roomTypeDto = roomTypeService.createRoomType(roomReq);

        LocalDate checkIn = LocalDate.now().plusDays(5);
        LocalDate checkOut = checkIn.plusDays(3);

        // Step 2: check availability.
        AvailabilityRequestDto availabilityRequest = new AvailabilityRequestDto();
        availabilityRequest.setRoomTypeId(roomTypeDto.getId());
        availabilityRequest.setCheckInDate(checkIn);
        availabilityRequest.setCheckOutDate(checkOut);
        availabilityRequest.setGuestsCount(2);
        AvailabilityResponseDto availability = availabilityPricingService.checkAvailabilityAndPrice(availabilityRequest);
        assertThat(availability.isAvailable()).isTrue();

        // Step 3: create booking (PENDING).
        BookingCreateRequest bookingRequest = new BookingCreateRequest();
        bookingRequest.setRoomTypeId(roomTypeDto.getId());
        bookingRequest.setCheckInDate(checkIn);
        bookingRequest.setCheckOutDate(checkOut);
        bookingRequest.setGuestsCount(2);
        bookingRequest.setGuestName("Test Guest");
        bookingRequest.setGuestEmail("guest@example.com");
        BookingDto bookingDto = bookingService.createBooking(bookingRequest);
        assertThat(bookingDto.getStatus()).isEqualTo(BookingStatus.PENDING);

        // Step 4: create payment intent and mark it as successful.
        PaymentDto paymentDto = paymentService.createPaymentIntent(bookingDto.getId());
        assertThat(paymentDto.getStatus()).isNotNull();

        paymentDto = paymentService.markSuccess(paymentDto.getId());
        assertThat(paymentDto.getStatus()).isEqualTo(com.swer313.hotel.monolith.payment.model.PaymentStatus.SUCCESS);

        // Step 5: booking should now be CONFIRMED.
        BookingDto confirmed = bookingService.getBooking(bookingDto.getId());
        assertThat(confirmed.getStatus()).isEqualTo(BookingStatus.CONFIRMED);
    }
}

