package com.dentalapp.backend.model.enums.exceptions;

public class PaymentStatusNotFoundException extends RuntimeException {
    public PaymentStatusNotFoundException() {
        super("Payment status not found");
    }
}
