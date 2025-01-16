package com.dentalapp.backend.model.treatment.dtos;

import com.dentalapp.backend.model.treatment.entity.Treatment;

public class TreatmentMapper {

    public static Treatment toEntityCreate(CreateTreatmentDto dto) {
        Treatment treatment = new Treatment();
        treatment.setTreatmentName(dto.getTreatmentName());
        treatment.setTreatmentDescription(dto.getTreatmentDescription());
        treatment.setTreatmentPrice(dto.getTreatmentPrice());
        return treatment;
    }

    public static Treatment toEntityUpdate(UpdateTreatmentDto dto, Treatment treatment) {
        if(dto.getTreatmentDescription() != null && !dto.getTreatmentDescription().equals(treatment.getTreatmentDescription())) {
            treatment.setTreatmentDescription(dto.getTreatmentDescription());
        }
        if(dto.getTreatmentPrice() != null && !dto.getTreatmentPrice().equals(treatment.getTreatmentPrice())) {
            treatment.setTreatmentPrice(dto.getTreatmentPrice());
        }
        if(dto.getTreatmentName() != null && !dto.getTreatmentName().equals(treatment.getTreatmentName())) {
            treatment.setTreatmentName(dto.getTreatmentName());
        }
        return treatment;
    }
}
