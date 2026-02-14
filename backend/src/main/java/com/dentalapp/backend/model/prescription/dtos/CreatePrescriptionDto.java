package com.dentalapp.backend.model.prescription.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreatePrescriptionDto {
    @NotNull(message = "Appointment ID is required")
    private Long appointmentId;

    @NotBlank(message = "Medicine is required")
    private String medicineName;

    @NotBlank(message = "Dosage is required")
    private String dosage;
}
