package com.dentalapp.backend.model.invoice.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetAppointmentPriceDto {

    @NotNull
    @Positive
    private Long price;
}
