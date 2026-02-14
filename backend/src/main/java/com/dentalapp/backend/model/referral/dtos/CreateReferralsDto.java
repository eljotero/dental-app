package com.dentalapp.backend.model.referral.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateReferralsDto {

    @NotNull
    private List<CreateReferralDto> createReferralDtoList;

    @NotNull
    private Long appointmentId;
}
