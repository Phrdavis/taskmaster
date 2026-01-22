package com.tasksmaster.taskmaster.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tasksmaster.taskmaster.dto.ErrorMessage;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) 
            throws IOException, ServletException {
        
        // Define o status e tipo de conteúdo
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Cria o seu DTO padronizado
        ErrorMessage error = new ErrorMessage(
                HttpStatus.UNAUTHORIZED.value(),
                "Acesso negado: você precisa estar autenticado para acessar este recurso.",
                LocalDateTime.now()
        );

        // Converte o objeto para JSON e escreve na resposta
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // Para lidar com o LocalDateTime
        response.getWriter().write(mapper.writeValueAsString(error));
    }
}