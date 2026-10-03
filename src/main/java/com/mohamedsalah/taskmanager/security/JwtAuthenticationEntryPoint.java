package com.mohamedsalah.taskmanager.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mohamedsalah.taskmanager.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Handles unauthenticated access attempts.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpServletResponse.SC_UNAUTHORIZED,
                "Unauthorized",
                "Unauthorized: " + authException.getMessage(),
                request.getRequestURI(),
                null
        );

        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        mapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
