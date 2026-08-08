package com.healthpay.payment_service.application;

import com.healthpay.payment_service.application.dto.ProcessPaymentRequest;
import com.healthpay.payment_service.domain.Payment;
import com.healthpay.payment_service.domain.PaymentRepository;
import com.healthpay.payment_service.domain.PaymentStatus;
import com.healthpay.payment_service.event.PaymentProcessedEvent;
import com.healthpay.payment_service.event.PaymentProducer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProcessPaymentUseCaseTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentProducer paymentProducer;

    @InjectMocks
    private ProcessPaymentUseCase processPaymentUseCase;

    @Test
    @DisplayName("Deve aprovar pagamento quando o valor for " +
            "menor ou igual ao limite de R$1000")
    void shouldApprovePaymentWhenAmoutIsWithinLimit() {
        UUID appointmentId = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("250.00");
        ProcessPaymentRequest request = new ProcessPaymentRequest(appointmentId, amount);

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> {
                    Payment p = invocation.getArgument(0);
                    p.setId(UUID.randomUUID());
                    return p;
                });

        Payment result = processPaymentUseCase.execute(request);

        assertNotNull(result);
        assertEquals(PaymentStatus.APPROVED,
                result.getStatus());
        assertEquals(amount, result.getAmount());

        ArgumentCaptor<PaymentProcessedEvent> eventCaptor = ArgumentCaptor.
                forClass(PaymentProcessedEvent.class);

        verify(paymentProducer, times(1))
                .sendPaymentProcessed(eventCaptor.capture());

        PaymentProcessedEvent publishedEvent = eventCaptor.getValue();
        assertEquals("APPROVED", publishedEvent.getStatus());
        assertEquals(amount, publishedEvent.getAmount());
    }

    @Test
    @DisplayName("Deve recusar (FAILED) pagamento quando o valor" +
            "ultrapassar R$1000")
    void shouldFailPaymentWhenAmountExceedsLimit() {
        UUID appointmentId = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("1500.00");

        ProcessPaymentRequest request = new ProcessPaymentRequest(appointmentId, amount);

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation ->{
                    Payment p = invocation.getArgument(0);
                    p.setId(UUID.randomUUID());
                    return p;
                });

        Payment result = processPaymentUseCase.execute(request);

        assertNotNull(result);
        assertEquals(PaymentStatus.FAILED,
                result.getStatus());

        ArgumentCaptor<PaymentProcessedEvent> eventCaptor = ArgumentCaptor.
                forClass(PaymentProcessedEvent.class);

        verify(paymentProducer, times(1))
                .sendPaymentProcessed(eventCaptor.capture());

        PaymentProcessedEvent publishedEvent = eventCaptor.getValue();
        assertEquals("FAILED", publishedEvent.getStatus());
    }
}
