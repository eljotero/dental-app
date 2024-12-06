package com.dentalapp.backend.model.treatment.dtos;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateTreatmentDto {

    private String name;

    private String description;

    @Positive(message = "Price must be positive")
    private Long price;
}
