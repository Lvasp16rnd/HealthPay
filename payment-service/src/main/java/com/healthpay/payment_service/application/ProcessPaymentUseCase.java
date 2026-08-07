package com.healthpay.payment_service.application;

import com.healthpay.payment_service.application.dto.ProcessPaymentRequest;
import com.healthpay.payment_service.domain.Payment;
import com.healthpay.payment_service.domain.PaymentRepository;
import com.healthpay.payment_service.domain.PaymentStatus;
import com.healthpay.payment_service.event.PaymentProcessedEvent;
import com.healthpay.payment_service.event.PaymentProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProcessPaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final PaymentProducer paymentProducer;

    public Payment execute(ProcessPaymentRequest request) {

        Payment payment = Payment.builder()
                .appointmentId(request.getAppointmentId())
                .amount(request.getAmount())
                .status(PaymentStatus.PENDING)
                .build();
        payment = paymentRepository.save(payment);

        payment.setStatus(PaymentStatus.PROCESSING);


        if (payment.getAmount().compareTo(new BigDecimal("1000.00")) > 0) {
            payment.setStatus(PaymentStatus.FAILED);
        } else {
            payment.setStatus(PaymentStatus.APPROVED);
        }

        Payment savedPayment = paymentRepository.save(payment);

        PaymentProcessedEvent event = PaymentProcessedEvent.builder()
                .paymentId(savedPayment.getId())
                .appointmentId(savedPayment.getAppointmentId())
                .amount(savedPayment.getAmount())
                .status(savedPayment.getStatus().name())
                .processedAt(LocalDateTime.now())
                .build();
        paymentProducer.sendPaymentProcessed(event);
        
        return savedPayment;
    }
}
