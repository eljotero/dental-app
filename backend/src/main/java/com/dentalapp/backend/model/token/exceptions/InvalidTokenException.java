package com.dentalapp.backend.model.token.exceptions;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() {
        super("Token not found");
    }
}
