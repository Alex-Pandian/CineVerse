package com.movie_app_backend.movie_app_backend.exception;

/**
 * EmailAlreadyExistsException - Exception thrown when trying to register with an existing email
 */
public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String message) {
        super(message);
    }

    public EmailAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}

