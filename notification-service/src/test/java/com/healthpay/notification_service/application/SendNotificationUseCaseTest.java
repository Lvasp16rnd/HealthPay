package com.healthpay.notification_service.application;

import com.healthpay.notification_service.application.dto.SendNotificationRequest;
import com.healthpay.notification_service.application.strategy.NotificationStrategy;
import com.healthpay.notification_service.domain.Notification;
import com.healthpay.notification_service.domain.NotificationChannel;
import com.healthpay.notification_service.domain.NotificationRepository;
import com.healthpay.notification_service.domain.NotificationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SendNotificationUseCaseTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationStrategy emailStrategy;

    private SendNotificationUseCase sendNotificationUseCase;

    @BeforeEach
    void setUp() {

        when(emailStrategy.getChannel()).thenReturn(NotificationChannel.EMAIL);
        sendNotificationUseCase = new SendNotificationUseCase(notificationRepository,
                List.of(emailStrategy));
    }

    @Test
    @DisplayName("Deve enviar notificação por Email e salvar " +
            "no banco com sucesso")
    void shouldSendEmailNotificationSuccessfully() {

        SendNotificationRequest request = new SendNotificationRequest(
                "paciente@email.com",
                "Consulta Agendada",
                "Sua consulta foi marcada com sucesso",
                NotificationChannel.EMAIL
        );

        when(notificationRepository.save(any(Notification.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        Notification result = sendNotificationUseCase.execute(request);

        assertNotNull(result);
        assertEquals(NotificationStatus.SENT, result.getStatus());
        assertEquals("paciente@email.com", result.getRecipient());

        verify(emailStrategy, times(1))
                .send(any(Notification.class));
        verify(notificationRepository, times(1))
                .save(any(Notification.class));
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException quando o canal não possuir estratégia cadastrada")
    void shouldThrowExceptionWhenStrategyNotFound() {
        SendNotificationRequest request = new SendNotificationRequest(
                "+5511999999999",
                "Aviso",
                "Mensagem SMS",
                NotificationChannel.SMS
        );

        assertThrows(IllegalArgumentException.class, () ->
                sendNotificationUseCase.execute(request));

        verify(notificationRepository, never()).save(any(Notification.class));
    }
}
