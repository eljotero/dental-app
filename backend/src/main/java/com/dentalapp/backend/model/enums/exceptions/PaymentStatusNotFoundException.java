package com.dentalapp.backend.model.enums.exceptions;

public class PaymentStatusNotFoundException extends RuntimeException {
    public PaymentStatusNotFoundException(String message) {
        super(message);
    }
}
