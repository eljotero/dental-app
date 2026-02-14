package com.dentalapp.backend.model.prescription.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreatePrescriptionsDto {
    @NotNull
    List<CreatePrescriptionDto> createPrescriptionsDtoList;

    @NotNull
    private Long appointmentId;
}
