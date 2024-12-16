package com.aluracursos.adopet.api.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class EmailServiceProduccionTest {

    @Mock
    private JavaMailSender emailSender;

    @Captor
    private ArgumentCaptor<SimpleMailMessage> emailMessageCaptor;

    @InjectMocks
    private EmailServiceProduccion emailService;

    @Test
    @DisplayName("Debería enviar los datos recibidos")
    void enviarEmail() {
        // ARRANGE
        String from = "adopet@email.com";
        String to = "destinatario@email.com";
        String subject = "asunto";
        String message = "mensaje";

        // ACT
        emailService.enviarEmail(to,subject,message);

        // ASSERT
        BDDMockito.then(emailSender).should(times(1)).send(emailMessageCaptor.capture());

        var emailMessageCapturado = emailMessageCaptor.getValue();

        String fromEnviado = emailMessageCapturado.getFrom();
        String toEnviado = emailMessageCapturado.getTo()[0];
        String subjectEnviado = emailMessageCapturado.getSubject();
        String messageEnviado = emailMessageCapturado.getText();

        Assertions.assertAll(
                () -> assertEquals(from, fromEnviado, "El campo 'from' no coincide"),
                () -> assertEquals(to, toEnviado, "El campo 'to' no coincide"),
                () -> assertEquals(subject, subjectEnviado, "El campo 'subject' no coincide"),
                () -> assertEquals(message, messageEnviado, "El campo 'message' no coincide")
        );

    }
}