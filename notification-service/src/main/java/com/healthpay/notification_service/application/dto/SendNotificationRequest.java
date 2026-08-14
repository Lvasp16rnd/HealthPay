package com.healthpay.notification_service.application.dto;

import com.healthpay.notification_service.domain.NotificationChannel;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendNotificationRequest {

    private String recipient;
    private String subject;
    private String message;
    private NotificationChannel channel;
}
