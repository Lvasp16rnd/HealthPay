package com.healthpay.payment_service.event;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentProducer {

    private static final String TOPIC = "payment.processed";
    private final KafkaTemplate<String, PaymentProcessedEvent> kafkaTemplate;

    public void sendPaymentProcessed(PaymentProcessedEvent event) {

        log.info("Publicando evento de pagamento processado no kafka [{}]: {}", TOPIC, event);
        kafkaTemplate.send(TOPIC, event.getAppointmentId().toString(), event);
    }
}
