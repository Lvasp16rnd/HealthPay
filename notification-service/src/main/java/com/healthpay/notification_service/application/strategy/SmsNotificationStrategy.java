package com.healthpay.notification_service.application.strategy;

import com.healthpay.notification_service.domain.Notification;
import com.healthpay.notification_service.domain.NotificationChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SmsNotificationStrategy implements NotificationStrategy {
    @Override
    public void send(Notification notification) {
        log.info("📱 [SMS] Enviado para {}, {}",
                notification.getRecipient(),
                notification.getMessage());
    }

    @Override
    public NotificationChannel getChannel() {
        return NotificationChannel.SMS;
    }
}
