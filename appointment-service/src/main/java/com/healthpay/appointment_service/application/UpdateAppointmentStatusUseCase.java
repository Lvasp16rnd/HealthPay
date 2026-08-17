package com.healthpay.appointment_service.application;

import com.healthpay.appointment_service.domain.Appointment;
import com.healthpay.appointment_service.domain.AppointmentRepository;
import com.healthpay.appointment_service.domain.AppointmentStatus;
import com.healthpay.appointment_service.event.AppointmentCompletedEvent;
import com.healthpay.appointment_service.event.AppointmentProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateAppointmentStatusUseCase {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentProducer appointmentProducer;

    public Appointment execute(UUID appointmentId, AppointmentStatus newStatus) {

        Appointment updateAppointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(RuntimeException::new);

        updateAppointment.setStatus(newStatus);
        Appointment savedAppointment = appointmentRepository.save(updateAppointment);

        if (newStatus == AppointmentStatus.COMPLETED) {
            AppointmentCompletedEvent event = AppointmentCompletedEvent.builder()
                    .appointmentId(savedAppointment.getId())
                    .patientId(savedAppointment.getPatientId())
                    .doctorId(savedAppointment.getDoctorId())
                    .amount(savedAppointment.getAmount())
                    .completedAt(java.time.LocalDateTime.now())
                    .build();
            appointmentProducer.sendAppointmentCompleted(event);
        }

        return savedAppointment;
    }
}
