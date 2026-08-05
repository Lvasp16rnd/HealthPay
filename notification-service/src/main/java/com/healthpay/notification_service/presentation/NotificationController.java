package com.healthpay.notification_service.presentation;

import com.healthpay.notification_service.application.SendNotificationUseCase;
import com.healthpay.notification_service.application.dto.SendNotificationRequest;
import com.healthpay.notification_service.domain.Notification;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final SendNotificationUseCase sendNotificationUseCase;

    @PostMapping
    public ResponseEntity<Notification> sendNotification(@RequestBody @Valid SendNotificationRequest request) {

        Notification notification = sendNotificationUseCase.execute(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(notification);
    }
}
