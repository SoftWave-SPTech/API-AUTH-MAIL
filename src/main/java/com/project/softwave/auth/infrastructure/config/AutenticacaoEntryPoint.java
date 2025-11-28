package com.project.softwave.auth.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class AutenticacaoEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", System.currentTimeMillis());
        errorDetails.put("path", request.getRequestURI());
        
        if (authException.getClass().equals(BadCredentialsException.class) || 
            authException.getClass().equals(InsufficientAuthenticationException.class)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            errorDetails.put("status", HttpServletResponse.SC_UNAUTHORIZED);
            errorDetails.put("error", "Unauthorized");
            errorDetails.put("message", authException.getMessage() != null ? authException.getMessage() : "Credenciais inválidas");
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            errorDetails.put("status", HttpServletResponse.SC_FORBIDDEN);
            errorDetails.put("error", "Forbidden");
            errorDetails.put("message", authException.getMessage() != null ? authException.getMessage() : "Acesso negado");
        }
        
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(errorDetails));
    }
}
