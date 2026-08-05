package com.healthpay.notification_service.application.dto;

import com.healthpay.notification_service.domain.NotificationChannel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendNotificationRequest {

    private String recipient;
    private String subject;
    private String message;
    private NotificationChannel channel;
}
