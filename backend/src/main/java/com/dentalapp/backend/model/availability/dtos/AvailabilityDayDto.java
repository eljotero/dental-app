package com.dentalapp.backend.model.availability.dtos;

import com.dentalapp.backend.model.user.entity.User;
import com.dentalapp.backend.utils.constraints.CreateTimesConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
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

    User doctor;
}