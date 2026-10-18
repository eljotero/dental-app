package com.dentalapp.backend.model.user.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("User not found");
    }

    private UserNotFoundException(String message) {
        super(message);
    }

    public static UserNotFoundException byEmail(String email) {
        return new UserNotFoundException("User with email " + email + " does not exist");
    }

    public static UserNotFoundException patientNotFound() {
        return new UserNotFoundException("Patient not found");
    }

    public static UserNotFoundException doctorNotFound() {
        return new UserNotFoundException("Doctor not found");
    }
}
