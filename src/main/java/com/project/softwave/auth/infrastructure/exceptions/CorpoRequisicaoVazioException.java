package com.project.softwave.auth.infrastructure.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CorpoRequisicaoVazioException extends RuntimeException {
    public CorpoRequisicaoVazioException(){}
    public CorpoRequisicaoVazioException(String message) {
        super(message);
    }
}

