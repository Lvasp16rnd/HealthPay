package com.healthpay.payment_service.presentation;

import com.healthpay.payment_service.application.ProcessPaymentUseCase;
import com.healthpay.payment_service.application.dto.ProcessPaymentRequest;
import com.healthpay.payment_service.domain.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final ProcessPaymentUseCase processPaymentUseCase;

    @PostMapping
    ResponseEntity<?> processPayment(@RequestBody ProcessPaymentRequest processPaymentRequest) {
        Payment savedPayment = processPaymentUseCase.execute(processPaymentRequest);

        return ResponseEntity.ok(savedPayment);
    }



}
