package com.dentalapp.backend.model.enums.exceptions;

public class PaymentMethodNotFoundException extends RuntimeException {
    public PaymentMethodNotFoundException() {
        super("Payment method not found");
    }
}
