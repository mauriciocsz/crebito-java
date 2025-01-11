package com.mauriciocsz.crebito.application.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.UNPROCESSABLE_ENTITY)
public class InvalidUserTransactionException extends RuntimeException {
    public InvalidUserTransactionException(String message) {
        super(message);
    }
}
