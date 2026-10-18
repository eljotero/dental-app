package com.dentalapp.backend.model.availability.dtos;

import com.dentalapp.backend.model.commons.dtos.SortField;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AvailabilitySortField implements SortField {
    ID("availabilityId"),
    DATE("availabilityDate"),
    START_TIME("availabilityStartTime"),
    END_TIME("availabilityEndTime");

    private final String property;
}
