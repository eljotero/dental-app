package com.dentalapp.backend.model.invoice.dtos;

import com.dentalapp.backend.model.commons.dtos.SortField;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InvoiceSortField implements SortField {
    ID("invoiceId"),
    PRICE("price"),
    PAYMENT_DATE("paymentDate"),
    STATUS("paymentStatus"),
    METHOD("paymentMethod");

    private final String property;
}
