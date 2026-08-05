package com.healthpay.notification_service.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "notifications")
@Getter @Setter @AllArgsConstructor
@NoArgsConstructor @Builder
public class Notification {
    @Id
    private String id;
    private String recipient;
    private String subject;
    private String message;
    private NotificationChannel channel;
    private NotificationStatus status;
    private LocalDateTime createdAt;
}
