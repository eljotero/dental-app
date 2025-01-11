package com.dentalapp.backend.model.referral.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetReferralDto {
    private String procedureName;
    private String procedureDescription;
    private String doctorName;
    private String clinicName;
    private String clinicAddress;
}
