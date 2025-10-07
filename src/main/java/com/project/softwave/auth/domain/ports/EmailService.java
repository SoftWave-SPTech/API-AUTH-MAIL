package com.project.softwave.auth.domain.ports;

public interface EmailService {
    
    void enviarEmailResetSenha(String email, String token);
    
    void enviarEmailPrimeiroAcesso(String email, String token);
}