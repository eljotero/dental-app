package com.dentalapp.backend.model.supplies.exceptions;

public class SupplyNotFoundException extends RuntimeException {
    public SupplyNotFoundException(String message) {
        super(message);
    }
}
