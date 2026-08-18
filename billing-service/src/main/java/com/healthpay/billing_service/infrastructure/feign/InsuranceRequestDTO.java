package com.healthpay.billing_service.infrastructure.feign;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceRequestDTO {
    private UUID invoiceId;
    private UUID patientId;
    private UUID doctorId;
    private BigDecimal amount;
}
