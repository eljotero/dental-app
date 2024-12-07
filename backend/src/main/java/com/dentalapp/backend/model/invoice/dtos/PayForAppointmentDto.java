package com.dentalapp.backend.model.invoice.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayForAppointmentDto {

    @Positive
    private Long price;

    @NotNull
    private String paymentType;

    @NotNull
    private String paymentDate;
}
