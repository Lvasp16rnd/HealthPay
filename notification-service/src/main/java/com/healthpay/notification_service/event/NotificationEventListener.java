package com.healthpay.notification_service.event;

import com.healthpay.notification_service.application.SendNotificationUseCase;
import com.healthpay.notification_service.application.dto.SendNotificationRequest;
import com.healthpay.notification_service.domain.NotificationChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final SendNotificationUseCase sendNotificationUseCase;

    @KafkaListener(topics = "appointment.created", groupId = "notification-group")
    public void handleAppointmentCreated(AppointmentCreatedEvent event){

        log.info("Notificando criação da consulta: {}", event.getAppointmentId());

        SendNotificationRequest request = SendNotificationRequest.builder()
                .recipient("paciente_" + event.getPatientId() + "@email.com")
                .message("Sua consulta foi agendada no valor de R$ " + event.getAmount() + ". Aguardando pagamento.")
                .channel(NotificationChannel.EMAIL)
                .build();

        sendNotificationUseCase.execute(request);
    }

    @KafkaListener(topics = "payment.processed", groupId = "notification-group")
    public void handlePaymentProcessed(PaymentProcessedEvent event){

        log.info("Notificando processamento do pagamento da consulta: {}", event.getPaymentId());

        String message = "APPROVED".equalsIgnoreCase(event.getStatus())
                ? "Seu pagamento de R$"+ event.getAmount()+ " foi aprovado com sucesso!"
                : "Seu pagamento de R$"+ event.getAmount()+ "FALHOU. Consulta cancelada!";

        SendNotificationRequest request = SendNotificationRequest.builder()
                .recipient("paciente@gmail.com")
                .message(message)
                .channel(NotificationChannel.EMAIL)
                .build();

        sendNotificationUseCase.execute(request);
    }
}
