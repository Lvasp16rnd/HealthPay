package com.healthpay.appointment_service.presentation;

import com.healthpay.appointment_service.application.CreateAppointmentUseCase;
import com.healthpay.appointment_service.application.UpdateAppointmentStatusUseCase;
import com.healthpay.appointment_service.application.dto.CreateAppointmentRequest;
import com.healthpay.appointment_service.application.dto.UpdateAppointmentStatusRequest;
import com.healthpay.appointment_service.domain.Appointment;
import com.healthpay.appointment_service.domain.AppointmentRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@Tag(name = "Appointments", description =
        "Endpoints para gerenciamento do ciclo de vida das consultas médicas")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final CreateAppointmentUseCase createAppointmentUseCase;
    private final AppointmentRepository appointmentRepository;
    private final UpdateAppointmentStatusUseCase updateAppointmentStatusUseCase;

    @Operation(summary = "Agendar nova consulta", description = "Cria um agendamento com status SCHEDULED " +
            "e publica o evento 'appointmentCreated' no Kafka")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta criada e evento publicado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos")
    })
    @PostMapping
    public ResponseEntity<?> createAppointment(@Valid @RequestBody CreateAppointmentRequest createAppointmentRequest) {


        Appointment savedAppointment = createAppointmentUseCase.execute(createAppointmentRequest);

        return ResponseEntity.ok(savedAppointment);
    }

    @Operation(summary = "Listar todas as consultas",
            description = "Retorna o histórico completo de consultas cadastradas")
    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return ResponseEntity.ok(appointmentRepository.findAll());
    }

    @Operation(summary = "Atualizar status da consulta",
            description = "Atualiza o status da consulta médica (ex: CONFIRMED, CANCELLED)")
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateAppointmentStatus(@PathVariable UUID id, @Valid @RequestBody UpdateAppointmentStatusRequest updateAppointmentStatusRequest) {

        Appointment updatedAppointment = updateAppointmentStatusUseCase.execute(id, updateAppointmentStatusRequest.getStatus());

        return ResponseEntity.ok(updatedAppointment);

    }
}
