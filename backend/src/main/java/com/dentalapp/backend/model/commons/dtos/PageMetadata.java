package com.dentalapp.backend.model.commons.dtos;

import org.springframework.data.domain.Page;

public record PageMetadata(int page, int size, int numberOfElements, long totalElements, int totalPages) {
    public static PageMetadata from(Page<?> result) {
        return new PageMetadata(result.getNumber(), result.getSize(), result.getNumberOfElements(), result.getTotalElements(), result.getTotalPages());
    }
}
