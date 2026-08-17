package com.healthpay.billing_service.application;

import com.healthpay.billing_service.domain.Invoice;
import com.healthpay.billing_service.domain.InvoiceRepository;
import com.healthpay.billing_service.domain.InvoiceStatus;
import com.healthpay.billing_service.event.AppointmentCompletedEvent;
import com.healthpay.billing_service.infrastructure.feign.HealthInsuranceClient;
import com.healthpay.billing_service.infrastructure.feign.InsuranceRequestDTO;
import com.healthpay.billing_service.infrastructure.feign.InsuranceResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenerateInvoiceUseCase {

    private final InvoiceRepository invoiceRepository;
    private final HealthInsuranceClient healthInsuranceClient;

    public void execute(AppointmentCompletedEvent event) {
        log.info("Iniciando faturamento para a consulta: {}", event.getAppointmentId());

        // 1. Cria a Fatura Local (Pendente)
        Invoice invoice = Invoice.builder()
                .appointmentId(event.getAppointmentId())
                .patientId(event.getPatientId())
                .doctorId(event.getDoctorId())
                .amount(event.getAmount())
                .status(InvoiceStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        invoiceRepository.save(invoice);

        // 2. Prepara o Payload para o Convênio
        InsuranceRequestDTO requestDTO = InsuranceRequestDTO.builder()
                .invoiceId(invoice.getId())
                .patientId(invoice.getPatientId())
                .doctorId(invoice.getDoctorId())
                .amount(invoice.getAmount())
                .build();

        try {
            // 3. Comunicação HTTP com o Convênio via Feign
            log.info("Enviando fatura {} para o Convênio Médico...", invoice.getId());
            InsuranceResponseDTO response = healthInsuranceClient.sendClaim(requestDTO);

            if ("ACCEPTED".equals(response.getStatus())) {
                invoice.setStatus(InvoiceStatus.SENT_TO_INSURANCE);
                log.info("Convênio aceitou a fatura. Protocolo: {}", response.getProtocolNumber());
            } else {
                invoice.setStatus(InvoiceStatus.FAILED);
                log.warn("Convênio rejeitou a fatura: {}", response.getMessage());
            }
        } catch (Exception e) {
            log.error("Erro de comunicação com a API do Convênio", e);
            invoice.setStatus(InvoiceStatus.FAILED);
        }

        invoice.setProcessedAt(LocalDateTime.now());
        invoiceRepository.save(invoice);
    }
}
