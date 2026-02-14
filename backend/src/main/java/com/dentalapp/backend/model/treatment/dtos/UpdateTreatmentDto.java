package com.dentalapp.backend.model.treatment.dtos;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTreatmentDto {

    private String treatmentName;

    private String treatmentDescription;

    @Positive(message = "Price must be positive")
    private Long treatmentPrice;
}
