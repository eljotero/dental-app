package com.dentalapp.backend.model.prescription.dtos;

import com.dentalapp.backend.model.commons.dtos.SortField;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PrescriptionSortField implements SortField {
    ID("prescriptionId"),
    MEDICINE("medicine"),
    CREATED_AT("createdAt");

    private final String property;
}
