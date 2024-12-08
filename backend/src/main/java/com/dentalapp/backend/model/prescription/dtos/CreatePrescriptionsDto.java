package com.dentalapp.backend.model.prescription.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePrescriptionsDto {
    @NotNull
    List<CreatePrescriptionDto> createPrescriptionsDtoList;

    @NotNull
    private Long appointmentId;
}
