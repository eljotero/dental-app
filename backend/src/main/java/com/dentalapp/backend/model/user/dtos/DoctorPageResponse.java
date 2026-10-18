package com.dentalapp.backend.model.user.dtos;

import com.dentalapp.backend.model.commons.dtos.PageMetadata;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.data.domain.Page;

import java.util.List;

public record DoctorPageResponse<T>(
        List<T> doctors,
        @JsonUnwrapped PageMetadata pagination
) {
    public static <T> DoctorPageResponse<T> from(Page<T> result) {
        return new DoctorPageResponse<>(result.getContent(), PageMetadata.from(result));
    }
}
