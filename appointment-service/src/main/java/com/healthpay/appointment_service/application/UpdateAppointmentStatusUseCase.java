package com.healthpay.appointment_service.application;

import com.healthpay.appointment_service.domain.Appointment;
import com.healthpay.appointment_service.domain.AppointmentRepository;
import com.healthpay.appointment_service.domain.AppointmentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateAppointmentStatusUseCase {

    private final AppointmentRepository appointmentRepository;

    public Appointment execute(UUID appointmentId, AppointmentStatus newStatus) {

        Appointment updateAppointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(RuntimeException::new);

        updateAppointment.setStatus(newStatus);

        return appointmentRepository.save(updateAppointment);
    }
}
