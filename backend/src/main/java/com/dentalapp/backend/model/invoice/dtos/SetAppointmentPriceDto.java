package com.dentalapp.backend.model.invoice.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SetAppointmentPriceDto {

    @NotNull
    @Positive
    private Long price;
}
