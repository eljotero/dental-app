package com.dentalapp.backend.model.prescription.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetPrescriptionDtoV2 {
    private Long prescriptionId;
    private String medicineName;
    private String dosage;
}

