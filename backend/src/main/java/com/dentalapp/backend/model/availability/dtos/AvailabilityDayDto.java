package com.dentalapp.backend.model.availability.dtos;

import com.dentalapp.backend.utils.constraints.CreateTimesConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@CreateTimesConstraint(
        startTime = "startTime",
        endTime = "endTime",
        message = "Start time must be before end time")
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