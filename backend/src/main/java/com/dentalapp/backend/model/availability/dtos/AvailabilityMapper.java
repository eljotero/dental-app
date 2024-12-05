package com.dentalapp.backend.model.availability.dtos;

import com.dentalapp.backend.model.availability.entity.Availability;

import java.time.LocalTime;

public class AvailabilityMapper {
    public static Availability toAvailability(AvailabilityDayDto availabilityDayDto) {
        Availability availability = new Availability();
        availability.setAvailabilityDate(availabilityDayDto.getDate());
        availability.setAvailabilityStartTime(LocalTime.parse(availabilityDayDto.getStartTime()));
        availability.setAvailabilityEndTime(LocalTime.parse(availabilityDayDto.getEndTime()));
        availability.setDoctor(availability.getDoctor());
        if(availabilityDayDto.getBrakeTimeStart() != null && availabilityDayDto.getBrakeTimeEnd() != null) {
            availability.setBrakeTimeStart(LocalTime.parse(availabilityDayDto.getBrakeTimeStart()));
            availability.setBrakeTimeEnd(LocalTime.parse(availabilityDayDto.getBrakeTimeEnd()));
        }
        return availability;
    }

    public static GetAvailabilityDto toDto(Availability availability) {
        GetAvailabilityDto availabilityDayDto = new GetAvailabilityDto();
        availabilityDayDto.setDate(availability.getAvailabilityDate());
        availabilityDayDto.setStartTime(availability.getAvailabilityStartTime());
        availabilityDayDto.setEndTime(availability.getAvailabilityEndTime());
        if(availability.getBrakeTimeStart() != null && availability.getBrakeTimeEnd() != null) {
            availabilityDayDto.setBrakeTimeStart(availability.getBrakeTimeStart());
            availabilityDayDto.setBrakeTimeEnd(availability.getBrakeTimeEnd());
        }
        availabilityDayDto.setConfirmed(availability.getIsConfirmed());
        return availabilityDayDto;
    }
}
