package com.healthpay.billing_service.infrastructure.feign;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceResponseDTO {
    private String protocolNumber;
    private String status; // ACCEPTED, REJECTED
    private String message;
}
