package com.dentalapp.backend.model.supplies.dtos;

import com.dentalapp.backend.model.commons.dtos.PageMetadata;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.data.domain.Page;

import java.util.List;

public record SupplyPageResponse<T>(
        List<T> supplies,
        @JsonUnwrapped PageMetadata pagination
) {
    public static <T> SupplyPageResponse<T> from(Page<T> result) {
        return new SupplyPageResponse<>(result.getContent(), PageMetadata.from(result));
    }
}
