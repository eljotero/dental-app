package com.dentalapp.backend.model.referral.exception;

public class ReferralNotFoundException extends RuntimeException {
    public ReferralNotFoundException(String message) {
        super(message);
    }
}
