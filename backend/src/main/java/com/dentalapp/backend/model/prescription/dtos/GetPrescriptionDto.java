package com.dentalapp.backend.model.prescription.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetPrescriptionDto {
    private Long prescriptionId;
    private String medicineName;
    private String dosage;
    private Long appointmentId;
}
