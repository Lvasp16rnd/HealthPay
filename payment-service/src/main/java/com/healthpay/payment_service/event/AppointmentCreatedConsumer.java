package com.healthpay.payment_service.event;


import com.healthpay.payment_service.application.ProcessPaymentUseCase;
import com.healthpay.payment_service.application.dto.ProcessPaymentRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppointmentCreatedConsumer {

    private final ProcessPaymentUseCase processPaymentUseCase;

    @KafkaListener(topics = "appointment.created", groupId = "payment-group")
    public void consume(AppointmentCreatedEvent event) {

        log.info("Recebido evento appointment.created no payment-service: {}", event);

        ProcessPaymentRequest request = new ProcessPaymentRequest(
                event.getAppointmentId(),
                event.getAmount()
        );

        processPaymentUseCase.execute(request);
    }
}
