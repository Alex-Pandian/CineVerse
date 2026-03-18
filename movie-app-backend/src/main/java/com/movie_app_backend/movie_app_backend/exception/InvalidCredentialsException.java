package com.movie_app_backend.movie_app_backend.exception;

/**
 * InvalidCredentialsException - Exception thrown when login credentials are invalid
 */
public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException(String message) {
        super(message);
    }

    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}

