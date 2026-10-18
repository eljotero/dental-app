package com.dentalapp.backend.model.token.exceptions;

public class TokenAlreadyUsedException extends RuntimeException {
    public TokenAlreadyUsedException() {
        super("Token already confirmed");
    }
}
