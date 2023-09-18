package com.github.register.infrastructure.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.register.domain.payload.response.CodeMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sniper
 * @date 17 Sep 2023
 */
@Component
public class AuthEntryPointJWT implements AuthenticationEntryPoint {

    private static final Logger log = LoggerFactory.getLogger(AuthEntryPointJWT.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("Unauthorized error: {}, Path:{}", authException.getMessage(), request.getServletPath());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        CodeMessage codeMessage = new CodeMessage();
        codeMessage.setCode(HttpServletResponse.SC_UNAUTHORIZED);
        codeMessage.setMessage("Unauthorized Access! Please check your Path or your authorization.");

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), codeMessage);
    }
}
