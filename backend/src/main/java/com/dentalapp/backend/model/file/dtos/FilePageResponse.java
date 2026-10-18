package com.dentalapp.backend.model.file.dtos;

import com.dentalapp.backend.model.commons.dtos.PageMetadata;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.data.domain.Page;

import java.util.List;

public record FilePageResponse<T>(
        List<T> files,
        @JsonUnwrapped PageMetadata pagination
) {
    public static <T> FilePageResponse<T> from(Page<T> result) {
        return new FilePageResponse<>(result.getContent(), PageMetadata.from(result));
    }
}
