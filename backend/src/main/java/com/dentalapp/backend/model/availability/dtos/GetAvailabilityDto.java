package com.dentalapp.backend.model.availability.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAvailabilityDto {

    LocalDate date;

    LocalTime startTime;

    LocalTime endTime;

    LocalTime brakeTimeStart;

    LocalTime brakeTimeEnd;

    boolean isConfirmed;
}
