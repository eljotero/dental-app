package com.dentalapp.backend.model.availability.exceptions;

public class AvailabilityAlreadyExistsException extends RuntimeException {
    public AvailabilityAlreadyExistsException(String message) {
        super(message);
    }
}
