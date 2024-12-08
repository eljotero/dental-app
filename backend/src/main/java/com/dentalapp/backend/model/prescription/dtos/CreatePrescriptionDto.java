package com.dentalapp.backend.model.prescription.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePrescriptionDto {
    @NotBlank(message = "Medicine is required")
    private String medicine;

    @NotBlank(message = "Dosage is required")
    private String dosage;
}
