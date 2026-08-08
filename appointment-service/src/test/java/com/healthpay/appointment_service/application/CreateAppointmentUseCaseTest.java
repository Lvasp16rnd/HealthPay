package com.healthpay.appointment_service.application;

import com.healthpay.appointment_service.application.dto.CreateAppointmentRequest;
import com.healthpay.appointment_service.domain.Appointment;
import com.healthpay.appointment_service.domain.AppointmentRepository;
import com.healthpay.appointment_service.domain.AppointmentStatus;
import com.healthpay.appointment_service.event.AppointmentCreatedEvent;
import com.healthpay.appointment_service.event.AppointmentProducer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateAppointmentUseCaseTest {
    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private AppointmentProducer appointmentProducer;

    @InjectMocks
    private CreateAppointmentUseCase createAppointmentUseCase;

    @Test
    @DisplayName("Deve criar consulta com status SCHEDULED " +
            "e publicar evento no kafka com sucesso")
    void shouldCreateAppointmentAndPublishEventSuccessfully() {

        UUID patientId = UUID.randomUUID();
        UUID doctorId = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("250.00");
        LocalDateTime date = LocalDateTime.now().plusDays(1);

        CreateAppointmentRequest request = new CreateAppointmentRequest(patientId,
                doctorId, date, amount);

        Appointment mockSavedAppointment = Appointment.builder()
                .id(UUID.randomUUID())
                .patientId(patientId)
                .doctorId(doctorId)
                .appointmentDate(date)
                .amount(amount)
                .status(AppointmentStatus.SCHEDULED)
                .build();

        when(appointmentRepository.save(any(Appointment.class))).thenReturn(mockSavedAppointment);

        Appointment result = createAppointmentUseCase.execute(request);

        assertNotNull(result);

        assertEquals(AppointmentStatus.SCHEDULED, result.getStatus());
        assertEquals(patientId, result.getPatientId());

        verify(appointmentRepository, times(1))
                .save(any(Appointment.class));
        verify(appointmentProducer, times(1))
                .sendAppointmentCreated(any
                        (AppointmentCreatedEvent.class));

    }
}
