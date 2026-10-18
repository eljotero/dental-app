package com.dentalapp.backend.model.prescription.dtos;

import com.dentalapp.backend.model.commons.dtos.PageMetadata;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.data.domain.Page;

import java.util.List;

public record PrescriptionPageResponse<T>(
        List<T> prescriptions,
        @JsonUnwrapped PageMetadata pagination
) {
    public static <T> PrescriptionPageResponse<T> from(Page<T> result) {
        return new PrescriptionPageResponse<>(result.getContent(), PageMetadata.from(result));
    }
}
