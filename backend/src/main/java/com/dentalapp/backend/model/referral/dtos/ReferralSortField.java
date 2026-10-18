package com.dentalapp.backend.model.referral.dtos;

import com.dentalapp.backend.model.commons.dtos.SortField;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReferralSortField implements SortField {
    ID("referralId"),
    PROCEDURE_NAME("procedureName"),
    CLINIC_NAME("clinicName"),
    CREATED_AT("createdAt");

    private final String property;
}
