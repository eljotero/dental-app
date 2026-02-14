package com.dentalapp.backend.services;

import com.dentalapp.backend.model.availability.dtos.*;
import com.dentalapp.backend.model.availability.entity.Availability;
import com.dentalapp.backend.model.availability.exceptions.AvailabilityAlreadyExistsException;
import com.dentalapp.backend.model.availability.exceptions.AvailabilityNotFoundException;
import com.dentalapp.backend.model.availability.repository.AvailabilityRepository;
import com.dentalapp.backend.model.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@AllArgsConstructor
public class AvailabilityService {

    private final AvailabilityRepository availabilityRepository;

    private final UserService userService;

    private final AvailabilityMapper availabilityMapper;

    public List<GetAvailabilityDto> getAllDoctorsAvailability(Specification<Availability> spec) {
        return availabilityRepository.findAll(spec).stream().map(availabilityMapper::toDto).toList();
    }

    public List<GetAvailabilityDto> getDoctorAvailability(Specification<Availability> spec) {
        return availabilityRepository.findAll(spec).stream().map(availabilityMapper::toDto).toList();
    }

    public List<Availability> getDoctorAvailability(Long doctorId, LocalDate date) {
        return availabilityRepository.findAllByDoctorUserIdAndAvailabilityDate(doctorId, date);
    }


    @Transactional
    public void addDoctorAvailability(CreateAvailabilityDto createAvailabilityDto, String email) {
        User doctor = userService.getDoctorByEmail(email);
        createAvailabilityDto.setDoctor(doctor);
        for (AvailabilityDayDto availabilityDayDto : createAvailabilityDto.getAvailabilityDays()) {
            if (!isAvailabilityExists(availabilityDayDto, doctor)) {
                Availability availability = availabilityMapper.toAvailability(availabilityDayDto, doctor);
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
        Availability updatedAvailability = availabilityMapper.updateAvailability(availability, updateAvailabilityDto);
        availabilityRepository.save(updatedAvailability);
    }

    @Transactional
    public boolean isDoctorAvailable(User doctor, LocalDate date, LocalTime startTime, LocalTime endTime) {
        return availabilityRepository.isDeclared(doctor, date, startTime, endTime);
    }


    private boolean isAvailabilityExists(AvailabilityDayDto availabilityDayDto, User doctor) {
        return availabilityRepository.hasDoctorAvailability(doctor, availabilityDayDto.getDate());
    }
}