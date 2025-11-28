package com.project.softwave.auth.domain.ports;

import org.springframework.security.core.Authentication;

public interface TokenService {
    
    String generateToken(Authentication authentication, String tipoUsuario, String nome, Integer id);
    
    String getUsernameFromToken(String token);
    
    boolean validateToken(String token, String username);
}