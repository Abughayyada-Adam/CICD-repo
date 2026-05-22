package com.swer313.hotel.monolith.payment.service;

import com.swer313.hotel.monolith.booking.model.Booking;
import com.swer313.hotel.monolith.booking.service.BookingService;
import com.swer313.hotel.monolith.common.error.BusinessException;
import com.swer313.hotel.monolith.common.error.NotFoundException;
import com.swer313.hotel.monolith.payment.dto.PaymentDto;
import com.swer313.hotel.monolith.payment.mapper.PaymentMapper;
import com.swer313.hotel.monolith.payment.model.Payment;
import com.swer313.hotel.monolith.payment.model.PaymentStatus;
import com.swer313.hotel.monolith.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * PaymentService models a very small "payment provider" that creates payment intents
 * and allows us to simulate success/failure/refund outcomes.
 */
@Service
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingService bookingService;

    public PaymentService(PaymentRepository paymentRepository, BookingService bookingService) {
        this.paymentRepository = paymentRepository;
        this.bookingService = bookingService;
    }

    /**
     * Create a new payment intent for a booking.
     * In a real system this would call an external provider such as Stripe.
     */
    public PaymentDto createPaymentIntent(Long bookingId) {
        Booking booking = bookingService.getBookingEntity(bookingId);
               

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(booking.getTotalPrice());
        payment.setCurrency("USD");
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(OffsetDateTime.now());
        payment.setUpdatedAt(payment.getCreatedAt());

        Payment saved = paymentRepository.save(payment);
        return PaymentMapper.toDto(saved);
    }

    public PaymentDto markSuccess(Long paymentId) {
        Payment payment = getPayment(paymentId);
        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            return PaymentMapper.toDto(payment);
        }
        if (payment.getStatus() == PaymentStatus.FAILED) {
            throw new BusinessException("Cannot mark a failed payment as successful");
        }
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setUpdatedAt(OffsetDateTime.now());

        // Confirm associated booking as part of the saga‑like flow.
        bookingService.confirmBooking(payment.getBooking().getId());
        return PaymentMapper.toDto(payment);
    }

    public PaymentDto markFailure(Long paymentId) {
        Payment payment = getPayment(paymentId);
        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            throw new BusinessException("Cannot fail an already successful payment");
        }
        if (payment.getStatus() == PaymentStatus.FAILED) {
            return PaymentMapper.toDto(payment);
        }
        payment.setStatus(PaymentStatus.FAILED);
        payment.setUpdatedAt(OffsetDateTime.now());
        return PaymentMapper.toDto(payment);
    }

    public PaymentDto refund(Long paymentId) {
        Payment payment = getPayment(paymentId);
        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            throw new BusinessException("Only successful payments can be refunded");
        }
        payment.setStatus(PaymentStatus.REFUNDED);
        payment.setUpdatedAt(OffsetDateTime.now());
        return PaymentMapper.toDto(payment);
    }

    public List<PaymentDto> listPaymentsForBooking(Long bookingId) {
        return paymentRepository.findByBookingId(bookingId)
                .stream()
                .map(PaymentMapper::toDto)
                .toList();
    }

    private Payment getPayment(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Payment with id " + id + " not found"));
    }
}

