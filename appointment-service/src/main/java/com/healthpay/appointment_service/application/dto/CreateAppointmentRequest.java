package com.healthpay.appointment_service.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateAppointmentRequest {

    @Schema(description = "ID do paciente", example =
    "11111111-1111-1111-1111-111111111111")
    private UUID patientId;

    @Schema(description = "ID do médico", example = "22222222-2222-2222-2222-222222222222")
    private UUID doctorId;

    @Schema(description = "Data e hora da consulta", example = "2026-08-10T14:30:00")
    private LocalDateTime appointmentDate;

    @Schema(description = "Valor da consulta em reais", example = "250.00")
    private BigDecimal amount;

}
