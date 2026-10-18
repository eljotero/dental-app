package com.dentalapp.backend.model.treatment.dtos;

import com.dentalapp.backend.model.commons.dtos.SortField;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TreatmentSortField implements SortField {
    ID("treatmentId"),
    NAME("treatmentName"),
    PRICE("treatmentPrice");

    private final String property;
}
