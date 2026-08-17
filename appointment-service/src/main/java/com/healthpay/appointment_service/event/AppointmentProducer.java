package com.healthpay.appointment_service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppointmentProducer {

    private static final String TOPIC_CREATED = "appointment.created";
    private static final String TOPIC_COMPLETED = "appointment.completed";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendAppointmentCreated(AppointmentCreatedEvent event) {
        log.info("Publicando evento no Kafka [{}]: {}", TOPIC_CREATED, event);
        kafkaTemplate.send(TOPIC_CREATED, event.getAppointmentId().toString(), event);
    }

    public void sendAppointmentCompleted(AppointmentCompletedEvent event) {
        log.info("Publicando evento no Kafka [{}]: {}", TOPIC_COMPLETED, event);
        kafkaTemplate.send(TOPIC_COMPLETED, event.getAppointmentId().toString(), event);
    }
}
