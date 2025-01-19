package com.dentalapp.backend.model.prescription.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePrescriptionDto {
    private String medicineName;
    private String dosage;
}
