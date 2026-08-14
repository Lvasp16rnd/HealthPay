package com.healthpay.notification_service.application.strategy;

import com.healthpay.notification_service.domain.Notification;
import com.healthpay.notification_service.domain.NotificationChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailNotificationStrategy implements NotificationStrategy{
    @Override
    public void send(Notification notification) {
        log.info("✉️ [EMAIL] Enviado para {}: {}",
                notification.getRecipient(),
                notification.getMessage());
    }

    @Override
    public NotificationChannel getChannel() {
        return NotificationChannel.EMAIL;
    }
}
