package com.dentalapp.backend.model.supplies.exceptions;

public class SupplyAlreadyExistsException extends RuntimeException {
    public SupplyAlreadyExistsException(String name) {
        super("Supply with name " + name + " already exists");
    }
}
