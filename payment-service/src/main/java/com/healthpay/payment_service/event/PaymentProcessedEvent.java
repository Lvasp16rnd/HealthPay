package com.healthpay.payment_service.event;

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
public class PaymentProcessedEvent {
    private UUID paymentId;
    private UUID appointmentId;
    private BigDecimal amount;
    private String status;
    private LocalDateTime processedAt;
}
