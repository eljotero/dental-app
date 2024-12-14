package com.dentalapp.backend.model.availability.dtos;

import com.dentalapp.backend.utils.constraints.UpdateTimesConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@UpdateTimesConstraint(
        startTime = "startTime",
        endTime = "endTime",
        message = "Start time must be before end time"
)
public class UpdateAvailabilityDto {

    private String startTime;

    private String endTime;

    private String brakeTimeStart;

    private String brakeTimeEnd;
}
