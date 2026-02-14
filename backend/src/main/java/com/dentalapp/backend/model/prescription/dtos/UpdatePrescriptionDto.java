package com.dentalapp.backend.model.prescription.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePrescriptionDto {
    private String medicineName;
    private String dosage;
}
