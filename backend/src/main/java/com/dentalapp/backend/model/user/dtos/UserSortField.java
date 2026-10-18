package com.dentalapp.backend.model.user.dtos;

import com.dentalapp.backend.model.commons.dtos.SortField;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserSortField implements SortField {
    ID("userId"),
    FIRST_NAME("firstName"),
    LAST_NAME("lastName"),
    EMAIL("email");

    private final String property;
}
