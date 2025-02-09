package com.dentalapp.backend.model.availability.dtos;

import com.dentalapp.backend.model.availability.entity.Availability;
import com.dentalapp.backend.model.user.entity.User;

import java.time.LocalTime;

public class AvailabilityMapper {
    public static Availability toAvailability(AvailabilityDayDto availabilityDayDto, User doctor) {
        Availability availability = new Availability();
        availability.setAvailabilityDate(availabilityDayDto.getDate());
        availability.setAvailabilityStartTime(LocalTime.parse(availabilityDayDto.getStartTime()));
        availability.setAvailabilityEndTime(LocalTime.parse(availabilityDayDto.getEndTime()));
        availability.setDoctor(doctor);
        return availability;
    }

    public static GetAvailabilityDto toDto(Availability availability) {
        GetAvailabilityDto availabilityDayDto = new GetAvailabilityDto();
        availabilityDayDto.setAvailabilityId(availability.getAvailabilityId());
        availabilityDayDto.setDate(availability.getAvailabilityDate());
        availabilityDayDto.setStartTime(availability.getAvailabilityStartTime());
        availabilityDayDto.setEndTime(availability.getAvailabilityEndTime());
        return availabilityDayDto;
    }

    public static Availability updateAvailability(Availability availability, UpdateAvailabilityDto updateAvailabilityDto) {
        if(updateAvailabilityDto.getStartTime() != null) {
            availability.setAvailabilityStartTime(LocalTime.parse(updateAvailabilityDto.getStartTime()));
        }
        if(updateAvailabilityDto.getEndTime() != null) {
            availability.setAvailabilityEndTime(LocalTime.parse(updateAvailabilityDto.getEndTime()));
        }
        return availability;
    }
}