package com.dentalapp.backend.model.availability.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailabilityDayDto {

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "Start time is required")
    private String startTime;

    @NotNull(message = "End time is required")
    private String endTime;

    private String brakeTimeStart;

    private String brakeTimeEnd;
}