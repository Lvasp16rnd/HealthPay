package com.healthpay.appointment_service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentCompletedEvent {
    private UUID appointmentId;
    private UUID patientId;
    private UUID doctorId;
    private BigDecimal amount;
    private LocalDateTime completedAt;
}
