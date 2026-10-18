package com.dentalapp.backend.model.user.dtos;

import com.dentalapp.backend.model.commons.dtos.PaginationParams;

import java.io.Serial;

public class UserQueryParams extends PaginationParams<UserSortField> {

    @Serial
    private static final long serialVersionUID = 1L;

    public UserQueryParams() {
        super(UserSortField.LAST_NAME, UserSortField.ID);
    }
}
