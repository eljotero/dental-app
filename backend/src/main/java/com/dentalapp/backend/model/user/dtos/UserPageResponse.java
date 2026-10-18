package com.dentalapp.backend.model.user.dtos;

import com.dentalapp.backend.model.commons.dtos.PageMetadata;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.data.domain.Page;

import java.util.List;

public record UserPageResponse<T>(
        List<T> users,
        @JsonUnwrapped PageMetadata pagination
) {
    public static <T> UserPageResponse<T> from(Page<T> result) {
        return new UserPageResponse<>(result.getContent(), PageMetadata.from(result));
    }
}
