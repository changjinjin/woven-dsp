package com.info.baymax.security.oauth.api.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CustomAuthenticationExceptionEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(final HttpServletRequest request, final HttpServletResponse response,
                         final AuthenticationException e) throws IOException, ServletException {
        Map<String, Object> result = new HashMap<String, Object>();
        if (e instanceof AuthenticationException) {
            if (e instanceof AuthenticationServiceException) {
                result.put("status", HttpStatus.FORBIDDEN.value());
                result.put("message", StringUtils.defaultIfEmpty(e.getMessage(), "Forbidden."));
            } else {
                result.put("status", HttpStatus.UNAUTHORIZED.value());
                result.put("message", StringUtils.defaultIfEmpty(e.getMessage(), "Unauthorized."));
            }
        } else {
            result.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.put("message", StringUtils.defaultIfEmpty(e.getMessage(), "Internal Server Error."));
        }
        result.put("path", request.getServletPath());
        result.put("timestamp", String.valueOf(new Date().getTime()));
        result.put("cause", e.getCause());
        response.setStatus(Integer.parseInt(result.get("status").toString()));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), result);
    }
}