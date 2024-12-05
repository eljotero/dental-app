package com.dentalapp.backend.model.availability.dtos;

import com.dentalapp.backend.model.user.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAvailabilityDto {

    @NotNull(message = "Availability is required")
    List<AvailabilityDayDto> availabilityDays;

    User doctor;
}
