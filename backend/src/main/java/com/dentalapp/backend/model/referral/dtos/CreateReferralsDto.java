package com.dentalapp.backend.model.referral.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateReferralsDto {

    @NotNull
    private List<CreateReferralDto> createReferralDtoList;

    @NotNull
    private Long appointmentId;
}
