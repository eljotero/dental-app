package com.dentalapp.backend.model.supplies.exceptions;

public class SupplyAlreadyExistsException extends RuntimeException {
    public SupplyAlreadyExistsException(String message) {
        super(message);
    }
}
