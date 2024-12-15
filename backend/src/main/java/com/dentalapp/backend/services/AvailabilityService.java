package com.dentalapp.backend.services;

import com.dentalapp.backend.model.availability.dtos.*;
import com.dentalapp.backend.model.availability.entity.Availability;
import com.dentalapp.backend.model.availability.exceptions.AvailabilityAlreadyExistsException;
import com.dentalapp.backend.model.availability.exceptions.AvailabilityNotFoundException;
import com.dentalapp.backend.model.availability.repository.AvailabilityRepository;
import com.dentalapp.backend.model.user.entity.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<GetAvailabilityDto> getAllDoctorsAvailability(Specification<Availability> spec) {
        return availabilityRepository.findAll(spec).stream().map(AvailabilityMapper::toDto).toList();
    }

    public List<GetAvailabilityDto> getDoctorAvailability(String email, LocalDate date) {
        User doctor = userService.getDoctorByEmail(email);
        if (date != null) {
            return getAllDoctorsAvailabilityByDate(doctor, date);
        }
        return availabilityRepository.findByDoctor(doctor).stream().map(AvailabilityMapper::toDto).toList();
    }


    @Transactional
    public void addDoctorAvailability(CreateAvailabilityDto createAvailabilityDto, String email) {
        User doctor = userService.getDoctorByEmail(email);
        createAvailabilityDto.setDoctor(doctor);
        for (AvailabilityDayDto availabilityDayDto : createAvailabilityDto.getAvailabilityDays()) {
            if (!isAvailabilityExists(availabilityDayDto, doctor)) {
                Availability availability = AvailabilityMapper.toAvailability(availabilityDayDto, doctor);
                availabilityRepository.save(availability);
            } else {
                throw new AvailabilityAlreadyExistsException("Availability already exists");
            }
        }
    }

    @Transactional
    public void confirmAvailability(Long id) {
        Availability availability = availabilityRepository.findById(id).orElseThrow(() -> new AvailabilityNotFoundException("Availability not found"));
        availability.setIsConfirmed(true);
        availabilityRepository.save(availability);
    }

    @Transactional
    public void updateAvailability(Long id, UpdateAvailabilityDto updateAvailabilityDto) {
        Availability availability = availabilityRepository.findById(id).orElseThrow(() -> new AvailabilityNotFoundException("Availability not found"));
        Availability updatedAvailability = AvailabilityMapper.updateAvailability(availability, updateAvailabilityDto);
        availabilityRepository.save(updatedAvailability);
    }

    @Transactional
    public boolean isDoctorAvailable(User doctor, LocalDate date, LocalTime startTime, LocalTime endTime) {
        return availabilityRepository.isDeclared(doctor, date, startTime, endTime);
    }

    private boolean isAvailabilityExists(AvailabilityDayDto availabilityDayDto, User doctor) {
        return availabilityRepository.hasDoctorAvailability(doctor, availabilityDayDto.getDate());
    }

    private List<GetAvailabilityDto> getAllDoctorsAvailabilityByDate(User doctor, LocalDate date) {
        List<Availability> availabilities = availabilityRepository.findByDoctorAndDate(doctor, date);
        return availabilities.stream().map(AvailabilityMapper::toDto).toList();
    }
}