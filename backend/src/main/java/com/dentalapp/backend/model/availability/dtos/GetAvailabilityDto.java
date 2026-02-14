package com.dentalapp.backend.model.availability.dtos;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetAvailabilityDto {

    Long availabilityId;

    LocalDate date;

    LocalTime startTime;

    LocalTime endTime;

    LocalTime brakeTimeStart;

    LocalTime brakeTimeEnd;

    boolean isConfirmed;
}
