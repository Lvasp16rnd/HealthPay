package com.healthpay.appointment_service.application.dto;

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

    private UUID patientId;
    private UUID doctorId;
    private LocalDateTime appointmentDate;
    private BigDecimal amount;

}
