package com.dentalapp.backend.model.referral.dtos;

import com.dentalapp.backend.model.commons.dtos.PaginationParams;

import java.io.Serial;

public class ReferralQueryParams extends PaginationParams<ReferralSortField> {

    @Serial
    private static final long serialVersionUID = 1L;

    public ReferralQueryParams() {
        super(ReferralSortField.ID, ReferralSortField.ID);
    }
}
