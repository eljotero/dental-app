package com.dentalapp.backend.model.treatment.dtos;

import com.dentalapp.backend.model.commons.dtos.PageMetadata;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.data.domain.Page;

import java.util.List;

public record TreatmentPageResponse<T>(
        List<T> treatments,
        @JsonUnwrapped PageMetadata pagination
) {
    public static <T> TreatmentPageResponse<T> from(Page<T> result) {
        return new TreatmentPageResponse<>(result.getContent(), PageMetadata.from(result));
    }
}
