package com.swer313.hotel.monolith.payment.repository;

import com.swer313.hotel.monolith.payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for payment entities.
 */
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByBookingId(Long bookingId);
}

