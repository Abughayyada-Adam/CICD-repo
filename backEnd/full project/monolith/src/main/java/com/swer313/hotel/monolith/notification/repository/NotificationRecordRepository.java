package com.swer313.hotel.monolith.notification.repository;

import com.swer313.hotel.monolith.notification.model.NotificationRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for storing notification records.
 */
public interface NotificationRecordRepository extends JpaRepository<NotificationRecord, Long> {
    List<NotificationRecord> findByRecipientEmailOrderByCreatedAtDesc(String email);
}

