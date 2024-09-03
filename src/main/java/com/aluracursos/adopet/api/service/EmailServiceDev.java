package com.aluracursos.adopet.api.service;

import org.springframework.stereotype.Service;

@Service
public class EmailServiceDev implements EmailService{

    public void enviarEmail(String to, String subject, String message) {
        System.out.println("Enviando email fake");
        System.out.println("Destinatario: " +to);
        System.out.println("Asunto: " +subject);
        System.out.println("Mensaje: " +message);
    }
}
