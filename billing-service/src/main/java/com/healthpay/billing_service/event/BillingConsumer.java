package com.healthpay.billing_service.event;

import com.healthpay.billing_service.application.GenerateInvoiceUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BillingConsumer {

    private final GenerateInvoiceUseCase generateInvoiceUseCase;

    @KafkaListener(topics = "appointment.completed", groupId = "billing-group")
    public void consumeAppointmentCompleted(AppointmentCompletedEvent event) {
        log.info("Evento Recebido no Kafka [appointment.completed]: {}", event);
        generateInvoiceUseCase.execute(event);
    }
}
