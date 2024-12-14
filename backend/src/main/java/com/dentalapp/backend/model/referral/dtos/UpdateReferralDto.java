package com.dentalapp.backend.model.referral.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateReferralDto {

    private String procedureName;

    private String procedureDescription;

    private String doctorName;

    private String clinicName;

    private String clinicAddress;
}
