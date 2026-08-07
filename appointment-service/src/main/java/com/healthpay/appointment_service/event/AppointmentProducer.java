package com.healthpay.appointment_service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppointmentProducer {

    private static final String TOPIC = "appointment.created";
    private final KafkaTemplate<String, AppointmentCreatedEvent> kafkaTemplate;

    public void sendAppointmentCreated(AppointmentCreatedEvent event) {

        log.info("Publicando evento no Kafka [{}]: {}", TOPIC, event);
        kafkaTemplate.send(TOPIC, event.getAppointmentId().toString(), event);
    }
}
