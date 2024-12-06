package com.dentalapp.backend.model.treatment.exceptions;

public class TreatmentAlreadyExistsException extends RuntimeException {
    public TreatmentAlreadyExistsException(String message) {
        super(message);
    }
}
