package com.dentalapp.backend.model.prescription.dtos;

import com.dentalapp.backend.model.prescription.entity.Prescription;

public class PrescriptionMapper {
    public static Prescription toEntity(CreatePrescriptionDto createPrescriptionDto) {
        Prescription prescription = new Prescription();
        prescription.setMedicine(createPrescriptionDto.getMedicine());
        prescription.setDosage(createPrescriptionDto.getDosage());
        return prescription;
    }

    public static Prescription toUpdateEntity(Prescription prescription, UpdatePrescriptionDto updatePrescriptionDto) {
        if(updatePrescriptionDto.getDosage() != null && !updatePrescriptionDto.getDosage().equals(prescription.getDosage())) {
            prescription.setDosage(updatePrescriptionDto.getDosage());
        }
        if(updatePrescriptionDto.getMedicine() != null && !updatePrescriptionDto.getMedicine().equals(prescription.getMedicine())) {
            prescription.setMedicine(updatePrescriptionDto.getMedicine());
        }
        return prescription;
    }
}
