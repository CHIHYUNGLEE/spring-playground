package com.chihyunglee.springplayground.security.exception;

import org.springframework.security.core.AuthenticationException;

public class UserWithdrawnException extends AuthenticationException {
    public UserWithdrawnException(String msg) {
        super(msg);
    }
}
