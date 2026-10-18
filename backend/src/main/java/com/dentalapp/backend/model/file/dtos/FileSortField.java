package com.dentalapp.backend.model.file.dtos;

import com.dentalapp.backend.model.commons.dtos.SortField;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileSortField implements SortField {
    ID("fileId"),
    NAME("fileName"),
    CREATED_AT("createdAt");

    private final String property;
}
