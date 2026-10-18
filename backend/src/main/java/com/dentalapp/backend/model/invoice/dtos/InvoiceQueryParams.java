package com.dentalapp.backend.model.invoice.dtos;

import com.dentalapp.backend.model.commons.dtos.PaginationParams;
import com.dentalapp.backend.model.enums.PaymentMethod;
import com.dentalapp.backend.model.enums.PaymentStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.time.LocalDate;

@Getter
@Setter
public class InvoiceQueryParams extends PaginationParams<InvoiceSortField> {

    @Serial
    private static final long serialVersionUID = 1L;

    public InvoiceQueryParams() {
        super(InvoiceSortField.ID, InvoiceSortField.ID);
    }

    private PaymentStatus status;
    private PaymentMethod method;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;
}
