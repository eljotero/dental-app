package com.dentalapp.backend.model.referral.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateReferralDto {

    @NotNull(message = "Appointment ID is required")
    private Long appointmentId;

    @NotBlank(message = "Procedure name is required")
    private String procedureName;

    @NotBlank(message = "Procedure description is required")
    private String procedureDescription;

    private String doctorName;

    @NotBlank(message = "Clinic name is required")
    private String clinicName;

    private String clinicAddress;
}
