package com.dentalapp.backend.model.supplies.dtos;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSupplyDto {
    private String name;

    @Positive
    private Double quantity;

    private String link;
}
