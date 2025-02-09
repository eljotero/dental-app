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

    public List<GetAvailabilityDto> getDoctorAvailability(Specification<Availability> spec) {
        return availabilityRepository.findAll(spec).stream().map(AvailabilityMapper::toDto).toList();
    }

    public List<Availability> getDoctorAvailability(Long doctorId, LocalDate date) {
        return availabilityRepository.findAllByDoctorUserIdAndAvailabilityDate(doctorId, date);
    }


    @Transactional
    public void addDoctorAvailability(AvailabilityDayDto createAvailabilityDto, String email) {
        User doctor = userService.getDoctorByEmail(email);
        createAvailabilityDto.setDoctor(doctor);
        if (!isAvailabilityExists(createAvailabilityDto, doctor)) {
            Availability availability = AvailabilityMapper.toAvailability(createAvailabilityDto, doctor);
            availabilityRepository.save(availability);
        } else {
            throw new AvailabilityAlreadyExistsException("Availability already exists");
        }
    }

    @Transactional
    public void updateAvailability(Long id, UpdateAvailabilityDto updateAvailabilityDto) {
        Availability availability = availabilityRepository.findById(id).orElseThrow(() -> new AvailabilityNotFoundException("Availability not found"));
        Availability updatedAvailability = AvailabilityMapper.updateAvailability(availability, updateAvailabilityDto);
        availabilityRepository.save(updatedAvailability);
    }

    @Transactional
    public void deleteAvailability(Long id) {
        availabilityRepository.deleteById(id);
    }

    @Transactional
    public boolean isDoctorAvailable(User doctor, LocalDate date, LocalTime startTime, LocalTime endTime) {
        return availabilityRepository.isDeclared(doctor, date, startTime, endTime);
    }

    public boolean isDoctorsAvailability(Long id, String email) {
        Availability availability =  availabilityRepository.getAvailabilityByAvailabilityId(id);
        return availability.getDoctor().getEmail().equals(email);
    }


    private boolean isAvailabilityExists(AvailabilityDayDto availabilityDayDto, User doctor) {
        return availabilityRepository.hasDoctorAvailability(doctor, availabilityDayDto.getDate(), LocalTime.parse(availabilityDayDto.getStartTime()), LocalTime.parse(availabilityDayDto.getEndTime()));
    }
}