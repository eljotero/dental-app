package com.dentalapp.backend.services;

import com.dentalapp.backend.model.availability.dtos.AvailabilityDayDto;
import com.dentalapp.backend.model.availability.dtos.AvailabilityMapper;
import com.dentalapp.backend.model.availability.dtos.CreateAvailabilityDto;
import com.dentalapp.backend.model.availability.dtos.GetAvailabilityDto;
import com.dentalapp.backend.model.availability.entity.Availability;
import com.dentalapp.backend.model.availability.exceptions.AvailabilityAlreadyExistsException;
import com.dentalapp.backend.model.availability.repository.AvailabilityRepository;
import com.dentalapp.backend.model.user.entity.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class AvailabilityService {

    private final AvailabilityRepository availabilityRepository;

    private final UserService userService;

    public AvailabilityService(AvailabilityRepository availabilityRepository, UserService userService) {
        this.availabilityRepository = availabilityRepository;
        this.userService = userService;
    }

    public void addDoctorAvailability(CreateAvailabilityDto createAvailabilityDto, String email) {
        User doctor = userService.getDoctorByEmail(email);
        createAvailabilityDto.setDoctor(doctor);
        for (AvailabilityDayDto availabilityDayDto : createAvailabilityDto.getAvailabilityDays()) {
            if (!isAvailabilityExists(availabilityDayDto, doctor)) {
                Availability availability = AvailabilityMapper.toAvailability(availabilityDayDto);
                availabilityRepository.save(availability);
            } else {
                throw new AvailabilityAlreadyExistsException("Availability already exists");
            }
        }
    }

    public List<GetAvailabilityDto> getDoctorAvailability(String email) {
        User doctor = userService.getDoctorByEmail(email);
        List<Availability> availabilities = availabilityRepository.findByDoctor(doctor);
        return availabilities.stream().map(AvailabilityMapper::toDto).toList();
    }

    public boolean isDoctorAvailable(User doctor, LocalDate date, LocalTime startTime, LocalTime endTime) {
        return availabilityRepository.isDeclared(doctor, date, startTime, endTime);
    }

    private boolean isAvailabilityExists(AvailabilityDayDto availabilityDayDto, User doctor) {
        boolean isDeclared = availabilityRepository.isDeclared(
                doctor,
                availabilityDayDto.getDate(),
                LocalTime.parse(availabilityDayDto.getStartTime()),
                LocalTime.parse(availabilityDayDto.getEndTime())
        );

        boolean hasDoctorBrake = false;
        if (availabilityDayDto.getBrakeTimeStart() != null && availabilityDayDto.getBrakeTimeEnd() != null) {
            hasDoctorBrake = availabilityRepository.hasDoctorBrake(
                    doctor,
                    availabilityDayDto.getDate(),
                    LocalTime.parse(availabilityDayDto.getBrakeTimeStart()),
                    LocalTime.parse(availabilityDayDto.getBrakeTimeEnd())
            );
        }

        return isDeclared || hasDoctorBrake;
    }
}