package com.dentalapp.backend.model.user.exceptions;

public class UserAuthenticationException extends RuntimeException {
    private UserAuthenticationException(String message) {
        super(message);
    }

    public static UserAuthenticationException accountDisabled(String email) {
        return new UserAuthenticationException("User with email " + email + " is not enabled");
    }

    public static UserAuthenticationException invalidCredentials() {
        return new UserAuthenticationException("Invalid email or password");
    }
}
