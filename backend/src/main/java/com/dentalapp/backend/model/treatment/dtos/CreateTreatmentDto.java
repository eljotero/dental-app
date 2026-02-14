package com.dentalapp.backend.model.treatment.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTreatmentDto {

    @NotNull(message = "Name is required")
    @NotBlank(message = "Name is required")
    private String treatmentName;

    @NotNull(message = "Description is required")
    @NotBlank(message = "Description is required")
    private String treatmentDescription;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Long treatmentPrice;
}
