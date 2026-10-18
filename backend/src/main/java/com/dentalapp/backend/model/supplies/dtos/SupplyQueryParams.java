package com.dentalapp.backend.model.supplies.dtos;

import com.dentalapp.backend.model.commons.dtos.PaginationParams;

import java.io.Serial;

public class SupplyQueryParams extends PaginationParams<SupplySortField> {

    @Serial
    private static final long serialVersionUID = 1L;

    public SupplyQueryParams() {
        super(SupplySortField.NAME, SupplySortField.ID);
    }
}
