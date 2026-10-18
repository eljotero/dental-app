package com.dentalapp.backend.model.prescription.dtos;

import com.dentalapp.backend.model.commons.dtos.PaginationParams;

import java.io.Serial;

public class PrescriptionQueryParams extends PaginationParams<PrescriptionSortField> {

    @Serial
    private static final long serialVersionUID = 1L;

    public PrescriptionQueryParams() {
        super(PrescriptionSortField.ID, PrescriptionSortField.ID);
    }
}
