package com.healthpay.notification_service.application.strategy;

import com.healthpay.notification_service.domain.Notification;
import com.healthpay.notification_service.domain.NotificationChannel;

public interface NotificationStrategy {
    void  send(Notification notification);
    NotificationChannel getChannel();
}
