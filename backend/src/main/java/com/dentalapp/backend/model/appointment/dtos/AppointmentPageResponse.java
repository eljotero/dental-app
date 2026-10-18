package com.dentalapp.backend.model.appointment.dtos;

import com.dentalapp.backend.model.commons.dtos.PageMetadata;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.data.domain.Page;

import java.util.List;

public record AppointmentPageResponse<T>(List<T> appointments, @JsonUnwrapped PageMetadata pagination) {
    public static <T> AppointmentPageResponse<T> from(Page<T> result) {
        return new AppointmentPageResponse<>(result.getContent(), PageMetadata.from(result));
    }
}
