package com.healthpay.billing_service.infrastructure.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "health-insurance-api", url = "${health-insurance.client.url}")
public interface HealthInsuranceClient {

    @PostMapping("/api/v1/claims")
    InsuranceResponseDTO sendClaim(@RequestBody InsuranceRequestDTO request);
}
