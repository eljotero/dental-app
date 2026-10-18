package com.dentalapp.backend.model.treatment.exceptions;

public class TreatmentNotFoundException extends RuntimeException {
    private TreatmentNotFoundException(String message) {
        super(message);
    }

    public static TreatmentNotFoundException byId(Long id) {
        return new TreatmentNotFoundException("Treatment with id " + id + " not found");
    }

    public static TreatmentNotFoundException byName(String name) {
        return new TreatmentNotFoundException("Treatment with name " + name + " not found");
    }
}
