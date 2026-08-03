package com.healthpay.appointment_service.application;

import com.healthpay.appointment_service.application.dto.CreateAppointmentRequest;
import com.healthpay.appointment_service.domain.Appointment;
import com.healthpay.appointment_service.domain.AppointmentRepository;
import com.healthpay.appointment_service.domain.AppointmentStatus;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class CreateAppointmentUseCase {

    private final AppointmentRepository appointmentRepository;

    public Appointment execute(CreateAppointmentRequest request) {

        Appointment newAppointment = Appointment.builder().patientId(request.getPatientId())
                .doctorId(request.getDoctorId()).appointmentDate(request.getAppointmentDate())
                .amount(request.getAmount()).status(AppointmentStatus.SCHEDULED).build();
        return appointmentRepository.save(newAppointment);
    }
}
