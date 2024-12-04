package com.dentalapp.backend.model.availability.dtos;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailabilityDayDto {

    @NotNull(message = "Date is required")
    @Past(message = "Date must be in the past")
    private LocalDate date;

    @NotNull(message = "Start time is required")
    @Past(message = "Date must be in the past")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    @Past(message = "Date must be in the past")
    private LocalTime endTime;

    @AssertTrue(message = "End time must be after start time")
    public boolean isEndTimeAfterStartTime() {
        return endTime.isAfter(startTime);
    }

    private LocalTime brakeTimeStart;

    private LocalTime brakeTimeEnd;

    @AssertTrue(message = "End time of brake must be after start time of brake")
    public boolean isBrakeEndTimeAfterStartTime() {
        if (brakeTimeStart == null || brakeTimeEnd == null) {
            return true;
        }
        return brakeTimeEnd.isAfter(brakeTimeStart);
    }
}
