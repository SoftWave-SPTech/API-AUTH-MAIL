package com.project.softwave.auth.infrastructure.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TokenExpiradoInvalidoException extends RuntimeException {
    public TokenExpiradoInvalidoException(){}
    public TokenExpiradoInvalidoException(String message) {
        super(message);
    }
}
