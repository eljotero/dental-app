package com.dentalapp.backend.model.prescription.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetPrescriptionDto {
    private Long prescriptionId;
    private String medicineName;
    private String dosage;
    private Long appointmentId;
}
