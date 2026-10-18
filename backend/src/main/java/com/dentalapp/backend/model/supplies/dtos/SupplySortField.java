package com.dentalapp.backend.model.supplies.dtos;

import com.dentalapp.backend.model.commons.dtos.SortField;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SupplySortField implements SortField {
    ID("supplyId"),
    NAME("name"),
    QUANTITY("quantity");

    private final String property;
}
