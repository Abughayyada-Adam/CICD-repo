package com.swer313.hotel.monolith.booking.repository;

import com.swer313.hotel.monolith.booking.model.Booking;
import com.swer313.hotel.monolith.booking.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Existing method (unchanged)
    @Query("""
            select b from Booking b
            where b.roomType.id = :roomTypeId
              and b.status in :statuses
              and b.checkInDate < :endDate
              and b.checkOutDate > :startDate
            """)
    List<Booking> findOverlappingBookings(@Param("roomTypeId") Long roomTypeId,
                                          @Param("statuses") List<BookingStatus> statuses,
                                          @Param("startDate") LocalDate startDate,
                                          @Param("endDate") LocalDate endDate);

    // NEW: Count duplicate bookings by same guest, room type, and overlapping dates
    @Query("""
            SELECT COUNT(b) FROM Booking b
            WHERE b.roomType.id = :roomTypeId
              AND b.guestEmail = :guestEmail
              AND b.status IN :statuses
              AND b.checkInDate < :endDate
              AND b.checkOutDate > :startDate
            """)
    long countExistingByGuestAndDates(@Param("roomTypeId") Long roomTypeId,
                                      @Param("guestEmail") String guestEmail,
                                      @Param("statuses") List<BookingStatus> statuses,
                                      @Param("startDate") LocalDate startDate,
                                      @Param("endDate") LocalDate endDate);

    // Existing method (unchanged)
    List<Booking> findByGuestEmailOrderByCheckInDateDesc(String guestEmail);

    List<Booking> findAllByOrderByCreatedAtDesc();

    List<Booking> findByStatusOrderByCreatedAtDesc(BookingStatus status);
}