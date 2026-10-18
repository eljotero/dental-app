package com.dentalapp.backend.model.treatment.dtos;

import com.dentalapp.backend.model.commons.dtos.PaginationParams;

import java.io.Serial;

public class TreatmentQueryParams extends PaginationParams<TreatmentSortField> {

    @Serial
    private static final long serialVersionUID = 1L;

    public TreatmentQueryParams() {
        super(TreatmentSortField.NAME, TreatmentSortField.ID);
    }
}
