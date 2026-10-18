package com.dentalapp.backend.model.referral.dtos;

import com.dentalapp.backend.model.commons.dtos.PageMetadata;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.data.domain.Page;

import java.util.List;

public record ReferralPageResponse<T>(
        List<T> referrals,
        @JsonUnwrapped PageMetadata pagination
) {
    public static <T> ReferralPageResponse<T> from(Page<T> result) {
        return new ReferralPageResponse<>(result.getContent(), PageMetadata.from(result));
    }
}
