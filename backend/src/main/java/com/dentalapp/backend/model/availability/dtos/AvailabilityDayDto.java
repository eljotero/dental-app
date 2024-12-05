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
    private LocalDate date;

    @NotNull(message = "Start time is required")
    private String startTime;

    @NotNull(message = "End time is required")
    private String endTime;

//    @AssertTrue(message = "End time must be after start time")
//    public boolean isEndTimeAfterStartTime() {
//        return endTime.isAfter(startTime);
//    }

    private String brakeTimeStart;

    private String brakeTimeEnd;

//    @AssertTrue(message = "End time of brake must be after start time of brake")
//    public boolean isBrakeEndTimeAfterStartTime() {
//        if (brakeTimeStart == null || brakeTimeEnd == null) {
//            return true;
//        }
//        return brakeTimeEnd.isAfter(brakeTimeStart);
//    }
}
