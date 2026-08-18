package com.healthpay.appointment_service.application;

import com.healthpay.appointment_service.domain.Appointment;
import com.healthpay.appointment_service.domain.AppointmentRepository;
import com.healthpay.appointment_service.domain.AppointmentStatus;
import com.healthpay.appointment_service.event.AppointmentCompletedEvent;
import com.healthpay.appointment_service.event.AppointmentProducer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateAppointmentUseCaseTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private AppointmentProducer appointmentProducer;

    @InjectMocks
    private UpdateAppointmentStatusUseCase updateAppointmentStatusUseCase;

    @Test
    @DisplayName("Deve atualizar o status da consulta com " +
            "sucesso quando ela existir")
    void shouldUpdateAppointmentStatusSuccessfully() {
        UUID appointmentId = UUID.randomUUID();
        Appointment existingAppointment = Appointment.builder()
                .id(appointmentId)
                .patientId(UUID.randomUUID())
                .doctorId(UUID.randomUUID())
                .appointmentDate(LocalDateTime.now())
                .amount(new BigDecimal("250.00"))
                .status(AppointmentStatus.SCHEDULED)
                .build();

        when(appointmentRepository.findById(appointmentId))
                .thenReturn(Optional.of(existingAppointment));

        when(appointmentRepository.save(any(Appointment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Appointment updated = updateAppointmentStatusUseCase.execute(appointmentId, AppointmentStatus.COMPLETED);

        assertNotNull(updated);

        assertEquals(AppointmentStatus.COMPLETED, updated.getStatus());
        verify(appointmentRepository, times(1)).findById(appointmentId);
        verify(appointmentRepository, times(1)).save(existingAppointment);
        verify(appointmentProducer, times(1)).sendAppointmentCompleted(any(AppointmentCompletedEvent.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o ID da consulta não for encontrado")
    void  shouldThrowExceptionWhenAppointmentNotFound() {
        UUID nonExistingId = UUID.randomUUID();
        when(appointmentRepository.findById(nonExistingId))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> updateAppointmentStatusUseCase
                .execute(nonExistingId, AppointmentStatus.COMPLETED));

        verify(appointmentRepository, times(1)).findById(nonExistingId);
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

}
