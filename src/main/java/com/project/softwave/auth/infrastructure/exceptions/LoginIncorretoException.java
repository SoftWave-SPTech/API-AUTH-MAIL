package com.project.softwave.auth.infrastructure.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class LoginIncorretoException extends RuntimeException {
    public LoginIncorretoException(){}
    public LoginIncorretoException(String message) {
        super(message);
    }
}
