package com.chihyunglee.springplayground.security.handler;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.chihyunglee.springplayground.security.exception.UserWithdrawnException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        Throwable cause = exception.getCause();
        if (cause instanceof UserWithdrawnException) {
            response.sendRedirect(request.getContextPath() + "/login?withdraw=true");
        } else {
            response.sendRedirect(request.getContextPath() + "/login?error=true");
        }
    }
}
