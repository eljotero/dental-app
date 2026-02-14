package com.dentalapp.backend.model.availability.dtos;

import com.dentalapp.backend.model.availability.entity.Availability;
import com.dentalapp.backend.model.user.entity.User;
import org.mapstruct.*;

import java.time.LocalTime;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AvailabilityMapper {

    @Mapping(target = "date", source = "availabilityDate")
    @Mapping(target = "startTime", source = "availabilityStartTime")
    @Mapping(target = "endTime", source = "availabilityEndTime")
    @Mapping(target = "confirmed", source = "isConfirmed")
    GetAvailabilityDto toDto(Availability availability);

    @Mapping(target = "availabilityStartTime", source = "startTime", qualifiedByName = "stringToLocalTime")
    @Mapping(target = "availabilityEndTime", source = "endTime", qualifiedByName = "stringToLocalTime")
    @Mapping(target = "brakeTimeStart", source = "brakeTimeStart", qualifiedByName = "stringToLocalTime")
    @Mapping(target = "brakeTimeEnd", source = "brakeTimeEnd", qualifiedByName = "stringToLocalTime")
    @Mapping(target = "availabilityId", ignore = true)
    @Mapping(target = "availabilityDate", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "isConfirmed", ignore = true)
    Availability updateAvailability(@MappingTarget Availability availability, UpdateAvailabilityDto updateAvailabilityDto);

    @Named("stringToLocalTime")
    default LocalTime stringToLocalTime(String time) {
        return time != null ? LocalTime.parse(time) : null;
    }

    @Mapping(target = "availabilityDate", source = "availabilityDayDto.date")
    @Mapping(target = "availabilityStartTime", source = "availabilityDayDto.startTime", qualifiedByName = "stringToLocalTime")
    @Mapping(target = "availabilityEndTime", source = "availabilityDayDto.endTime", qualifiedByName = "stringToLocalTime")
    @Mapping(target = "brakeTimeStart", source = "availabilityDayDto.brakeTimeStart", qualifiedByName = "stringToLocalTime")
    @Mapping(target = "brakeTimeEnd", source = "availabilityDayDto.brakeTimeEnd", qualifiedByName = "stringToLocalTime")
    @Mapping(target = "doctor", source = "doctor")
    @Mapping(target = "availabilityId", ignore = true)
    @Mapping(target = "isConfirmed", ignore = true)
    Availability toAvailability(AvailabilityDayDto availabilityDayDto, User doctor);

}
