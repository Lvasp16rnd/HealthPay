package com.healthpay.notification_service.application;

import com.healthpay.notification_service.application.dto.SendNotificationRequest;
import com.healthpay.notification_service.application.strategy.NotificationStrategy;
import com.healthpay.notification_service.domain.Notification;
import com.healthpay.notification_service.domain.NotificationChannel;
import com.healthpay.notification_service.domain.NotificationRepository;
import com.healthpay.notification_service.domain.NotificationStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SendNotificationUseCase {
    private final NotificationRepository notificationRepository;
    private final Map<NotificationChannel, NotificationStrategy> strategies;

    public SendNotificationUseCase(NotificationRepository notificationRepository,
                                   List<NotificationStrategy> strategyList) {
        this.notificationRepository = notificationRepository;
        this.strategies = strategyList.stream().collect(Collectors.toMap(NotificationStrategy::getChannel,
                Function.identity()));
    }

    public Notification execute(SendNotificationRequest request) {
            NotificationStrategy strategy = strategies.get(request.getChannel());

            if (strategy == null) {
                throw new IllegalArgumentException("Canal de notificação não suportado " + request.getChannel());
            }

            Notification notification = Notification.builder()
                    .recipient(request.getRecipient())
                    .subject(request.getSubject())
                    .message(request.getMessage())
                    .channel(request.getChannel())
                    .status(NotificationStatus.SENT)
                    .createdAt(LocalDateTime.now())
                    .build();

            strategy.send(notification);


        return notificationRepository.save(notification);
    }
}

