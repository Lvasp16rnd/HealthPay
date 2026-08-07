package com.healthpay.appointment_service.application;

import com.healthpay.appointment_service.application.dto.CreateAppointmentRequest;
import com.healthpay.appointment_service.domain.Appointment;
import com.healthpay.appointment_service.domain.AppointmentRepository;
import com.healthpay.appointment_service.domain.AppointmentStatus;
import com.healthpay.appointment_service.event.AppointmentCreatedEvent;
import com.healthpay.appointment_service.event.AppointmentProducer;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class CreateAppointmentUseCase {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentProducer appointmentProducer;

    public Appointment execute(CreateAppointmentRequest request) {

        Appointment newAppointment = Appointment.builder().patientId(request.getPatientId())
                .doctorId(request.getDoctorId()).appointmentDate(request.getAppointmentDate())
                .amount(request.getAmount()).status(AppointmentStatus.SCHEDULED).build();

        Appointment savedAppointment = appointmentRepository.save(newAppointment);

        AppointmentCreatedEvent event = AppointmentCreatedEvent.builder()
                .appointmentId(savedAppointment.getId())
                .patientId(savedAppointment.getPatientId())
                .doctorId(savedAppointment.getDoctorId())
                .amount(savedAppointment.getAmount())
                .dateTime(savedAppointment.getAppointmentDate())
                .build();

        appointmentProducer.sendAppointmentCreated(event);

        return savedAppointment;
    }

}
