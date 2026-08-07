package com.healthpay.appointment_service.event;

import com.healthpay.appointment_service.application.UpdateAppointmentStatusUseCase;
import com.healthpay.appointment_service.domain.AppointmentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentProcessConsumer {

    private final UpdateAppointmentStatusUseCase updateAppointmentStatusUseCase;

    @KafkaListener(topics = "payment.processed", groupId = "appointment-group")
    public void consume(PaymentProcessedEvent event) {

        log.info("Recebido evento payment-processed no appointment-service: {}", event);

        AppointmentStatus finalStatus = "APPROVED".equalsIgnoreCase(event.getStatus())
                ? AppointmentStatus.COMPLETED
                : AppointmentStatus.CANCELLED;

        updateAppointmentStatusUseCase.execute(event.getAppointmentId(), finalStatus);
        log.info("Status da consulta {} atualizado para {}", event.getAppointmentId(),
                finalStatus);
    }
}
