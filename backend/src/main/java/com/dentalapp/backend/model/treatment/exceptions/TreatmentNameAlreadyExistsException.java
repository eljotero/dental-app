package com.dentalapp.backend.model.treatment.exceptions;

public class TreatmentNameAlreadyExistsException extends RuntimeException {
    public TreatmentNameAlreadyExistsException(String message) {
        super(message);
    }
}
