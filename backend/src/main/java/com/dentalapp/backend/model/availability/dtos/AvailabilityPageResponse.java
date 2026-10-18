package com.dentalapp.backend.model.availability.dtos;

import com.dentalapp.backend.model.commons.dtos.PageMetadata;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.data.domain.Page;

import java.util.List;

public record AvailabilityPageResponse<T>(List<T> availabilities, @JsonUnwrapped PageMetadata pagination) {
    public static <T> AvailabilityPageResponse<T> from(Page<T> result) {
        return new AvailabilityPageResponse<>(result.getContent(), PageMetadata.from(result));
    }
}
