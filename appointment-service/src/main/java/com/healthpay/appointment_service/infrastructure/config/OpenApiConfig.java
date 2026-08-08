package com.healthpay.appointment_service.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("HealthPay - Appointment Service API")
                        .version("v1.0")
                        .description("API REST para agendamento de consultas" +
                                " médicas e integração orientada a eventos com Apache Kafka.")
                        .contact(new Contact()
                                .name("Lucas")
                                .url("https://github.com/Lvasp16rnd")));
    }
}
