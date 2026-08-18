package com.healthpay.billing_service.application;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.healthpay.billing_service.domain.Invoice;
import com.healthpay.billing_service.domain.InvoiceRepository;
import com.healthpay.billing_service.domain.InvoiceStatus;
import com.healthpay.billing_service.event.AppointmentCompletedEvent;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class GenerateInvoiceUseCaseIntegrationTest {

    @Autowired
    private GenerateInvoiceUseCase generateInvoiceUseCase;

    @Autowired
    private InvoiceRepository invoiceRepository;

    private static WireMockServer wireMockServer;

    @BeforeAll
    static void startWireMock() {
        wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
        wireMockServer.start();
    }

    @AfterAll
    static void stopWireMock() {
        wireMockServer.stop();
    }

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("health-insurance.client.url", wireMockServer::baseUrl);
        registry.add("spring.kafka.bootstrap-servers", () -> "localhost:9092"); // Mock placeholder if needed
    }

    @Test
    @DisplayName("Deve faturar e marcar como SENT_TO_INSURANCE quando o convênio retornar ACCEPTED")
    void shouldInvoiceAndSendToInsuranceSuccessfully() {
        wireMockServer.stubFor(post(urlEqualTo("/api/v1/claims"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                                "{\"protocolNumber\":\"PROTO-123\", \"status\":\"ACCEPTED\", \"message\":\"Reembolso Autorizado\"}")));

        AppointmentCompletedEvent event = AppointmentCompletedEvent.builder()
                .appointmentId(UUID.randomUUID())
                .patientId(UUID.randomUUID())
                .doctorId(UUID.randomUUID())
                .amount(new BigDecimal("250.00"))
                .build();

        generateInvoiceUseCase.execute(event);

        List<Invoice> invoices = invoiceRepository.findAll();
        assertEquals(1, invoices.size());

        Invoice savedInvoice = invoices.get(0);
        assertEquals(InvoiceStatus.SENT_TO_INSURANCE, savedInvoice.getStatus());
        assertEquals(event.getAppointmentId(), savedInvoice.getAppointmentId());

        invoiceRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve faturar mas marcar como FAILED quando o convênio der erro 500")
    void shouldInvoiceAndMarkAsFailedWhenInsuranceFails() {
        wireMockServer.stubFor(post(urlEqualTo("/api/v1/claims"))
                .willReturn(aResponse()
                        .withStatus(500)
                        .withBody("Internal Server Error")));

        AppointmentCompletedEvent event = AppointmentCompletedEvent.builder()
                .appointmentId(UUID.randomUUID())
                .patientId(UUID.randomUUID())
                .doctorId(UUID.randomUUID())
                .amount(new BigDecimal("500.00"))
                .build();

        generateInvoiceUseCase.execute(event);

        List<Invoice> invoices = invoiceRepository.findAll();
        assertEquals(1, invoices.size());

        Invoice savedInvoice = invoices.get(0);
        assertEquals(InvoiceStatus.FAILED, savedInvoice.getStatus());

        invoiceRepository.deleteAll();
    }
}
