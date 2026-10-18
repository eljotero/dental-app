package com.dentalapp.backend.model.availability.exceptions;

public class AvailabilityNotFoundException extends RuntimeException {
    public AvailabilityNotFoundException() {
        super("Availability not found");
    }
}
