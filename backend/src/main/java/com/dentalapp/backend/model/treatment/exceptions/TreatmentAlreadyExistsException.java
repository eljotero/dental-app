package com.dentalapp.backend.model.treatment.exceptions;

public class TreatmentAlreadyExistsException extends RuntimeException {
    public TreatmentAlreadyExistsException(String name) {
        super("Treatment with name " + name + " already exists");
    }
}
