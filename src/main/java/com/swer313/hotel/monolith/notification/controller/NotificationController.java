package com.swer313.hotel.monolith.notification.controller;

import com.swer313.hotel.monolith.notification.model.NotificationRecord;
import com.swer313.hotel.monolith.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API for retrieving notification history.
 */
@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notification", description = "View notification history")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/guest/{email}")
    @Operation(summary = "Get notification history for a guest by email")
    public List<NotificationRecord> getNotifications(@PathVariable String email) {
        return notificationService.getNotificationsForGuest(email);
    }
}
