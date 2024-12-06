package com.dentalapp.backend.model.treatment.dtos;

import com.dentalapp.backend.model.treatment.entity.Treatment;

public class TreatmentMapper {

    public static Treatment toEntityCreate(CreateTreatmentDto dto) {
        Treatment treatment = new Treatment();
        treatment.setTreatmentName(dto.getName());
        treatment.setTreatmentDescription(dto.getDescription());
        treatment.setTreatmentPrice(dto.getPrice());
        return treatment;
    }

    public static Treatment toEntityUpdate(UpdateTreatmentDto dto, Treatment treatment) {
        if(dto.getDescription() != null && !dto.getDescription().equals(treatment.getTreatmentDescription())) {
            treatment.setTreatmentDescription(dto.getDescription());
        }
        if(dto.getPrice() != null && !dto.getPrice().equals(treatment.getTreatmentPrice())) {
            treatment.setTreatmentPrice(dto.getPrice());
        }
        if(dto.getName() != null && !dto.getName().equals(treatment.getTreatmentName())) {
            treatment.setTreatmentName(dto.getName());
        }
        return treatment;
    }
}
