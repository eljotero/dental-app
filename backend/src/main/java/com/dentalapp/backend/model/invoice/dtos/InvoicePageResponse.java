package com.dentalapp.backend.model.invoice.dtos;

import com.dentalapp.backend.model.commons.dtos.PageMetadata;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.data.domain.Page;

import java.util.List;

public record InvoicePageResponse<T>(
        List<T> invoices,
        @JsonUnwrapped PageMetadata pagination
) {
    public static <T> InvoicePageResponse<T> from(Page<T> result) {
        return new InvoicePageResponse<>(result.getContent(), PageMetadata.from(result));
    }
}
