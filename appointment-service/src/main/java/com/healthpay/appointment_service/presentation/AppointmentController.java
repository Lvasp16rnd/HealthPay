package com.healthpay.appointment_service.presentation;

import com.healthpay.appointment_service.application.CreateAppointmentUseCase;
import com.healthpay.appointment_service.application.UpdateAppointmentStatusUseCase;
import com.healthpay.appointment_service.application.dto.CreateAppointmentRequest;
import com.healthpay.appointment_service.application.dto.UpdateAppointmentStatusRequest;
import com.healthpay.appointment_service.domain.Appointment;
import com.healthpay.appointment_service.domain.AppointmentRepository;
import com.healthpay.appointment_service.domain.AppointmentStatus;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final CreateAppointmentUseCase createAppointmentUseCase;
    private final AppointmentRepository appointmentRepository;
    private final UpdateAppointmentStatusUseCase updateAppointmentStatusUseCase;

    @PostMapping
    public ResponseEntity<?> createAppointment(@Valid @RequestBody CreateAppointmentRequest createAppointmentRequest) {


        Appointment savedAppointment = createAppointmentUseCase.execute(createAppointmentRequest);

        return ResponseEntity.ok(savedAppointment);
    }

    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return ResponseEntity.ok(appointmentRepository.findAll());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateAppointmentStatus(@PathVariable UUID id, @Valid @RequestBody UpdateAppointmentStatusRequest updateAppointmentStatusRequest) {

        Appointment updatedAppointment = updateAppointmentStatusUseCase.execute(id, updateAppointmentStatusRequest.getStatus());

        return ResponseEntity.ok(updatedAppointment);

    }
}
