package com.swer313.hotel.monolith.notification.service;

import com.swer313.hotel.monolith.booking.model.Booking;
import com.swer313.hotel.monolith.notification.model.NotificationRecord;
import com.swer313.hotel.monolith.notification.model.NotificationType;
import com.swer313.hotel.monolith.notification.repository.NotificationRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * NotificationService is intentionally simple: instead of integrating with a real email provider
 * it writes records into the database and logs them, which is enough to demonstrate the flow.
 */
@Service
@Transactional
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRecordRepository notificationRecordRepository;

    public NotificationService(NotificationRecordRepository notificationRecordRepository) {
        this.notificationRecordRepository = notificationRecordRepository;
    }

    public void sendBookingConfirmation(Booking booking) {
        String subject = "Your booking is confirmed";
        String body = "Dear " + booking.getGuestName() + ", your booking #" + booking.getId()
                + " at " + booking.getHotel().getName()
                + " from " + booking.getCheckInDate()
                + " to " + booking.getCheckOutDate()
                + " is confirmed.";
        persistAndLog(booking, NotificationType.BOOKING_CONFIRMED, subject, body);
    }

    public void sendBookingCancellation(Booking booking) {
        String subject = "Your booking has been cancelled";
        String body = "Dear " + booking.getGuestName() + ", your booking #" + booking.getId()
                + " at " + booking.getHotel().getName()
                + " has been cancelled. Reason: " + booking.getCancellationReason();
        persistAndLog(booking, NotificationType.BOOKING_CANCELLED, subject, body);
    }

    public List<NotificationRecord> getNotificationsForGuest(String email) {
        return notificationRecordRepository.findByRecipientEmailOrderByCreatedAtDesc(email);
    }

    private void persistAndLog(Booking booking, NotificationType type, String subject, String body) {
        NotificationRecord record = new NotificationRecord();
        record.setType(type);
        record.setBookingId(booking.getId());
        record.setRecipientEmail(booking.getGuestEmail());
        record.setSubject(subject);
        record.setBody(body);
        record.setCreatedAt(OffsetDateTime.now());
        notificationRecordRepository.save(record);

        // Logging helps visualise notifications while developing locally.
        log.info("Notification sent: type={}, bookingId={}, to={}, subject={}",
                type, booking.getId(), booking.getGuestEmail(), subject);
    }
}

