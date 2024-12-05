package com.dentalapp.backend.unit.services;

import com.dentalapp.backend.model.availability.dtos.AvailabilityDayDto;
import com.dentalapp.backend.model.availability.dtos.CreateAvailabilityDto;
import com.dentalapp.backend.model.availability.exceptions.AvailabilityAlreadyExistsException;
import com.dentalapp.backend.model.availability.repository.AvailabilityRepository;
import com.dentalapp.backend.model.enums.UserType;
import com.dentalapp.backend.model.user.entity.User;
import com.dentalapp.backend.services.AvailabilityService;
import com.dentalapp.backend.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AvailabilityServiceTests {

    @Mock
    private AvailabilityRepository availabilityRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private AvailabilityService availabilityService;

    CreateAvailabilityDto createAvailabilityDto;

    AvailabilityDayDto availabilityDayDto;

    User user;

    String email;

    @BeforeEach
    public void setUp() {
        email = "test@mail.com";
        user = new User();
        user.setUserId(1L);
        user.setUserType(UserType.DOCTOR);
        createAvailabilityDto = new CreateAvailabilityDto();
        availabilityDayDto = new AvailabilityDayDto();
        availabilityDayDto.setDate(LocalDate.of(2021, 10, 10));
        availabilityDayDto.setStartTime("08:00");
        availabilityDayDto.setEndTime("16:00");
        availabilityDayDto.setBrakeTimeStart("12:00");
        availabilityDayDto.setBrakeTimeEnd("13:00");
        createAvailabilityDto.setAvailabilityDays(List.of(availabilityDayDto));
    }


    @Test
    public void testAddDoctorAvailability() {
        when(userService.getDoctorByEmail(email)).thenReturn(user);
        availabilityService.addDoctorAvailability(createAvailabilityDto, email);
    }

    @Test
    public void testAddDoctorAvailabilityAlreadyExists() {
        when(userService.getDoctorByEmail(email)).thenReturn(user);
        when(availabilityRepository.isDeclared(user, availabilityDayDto.getDate(), LocalTime.parse(availabilityDayDto.getStartTime()), LocalTime.parse(availabilityDayDto.getEndTime()))).thenReturn(true);
        Assertions.assertThrows(AvailabilityAlreadyExistsException.class, () -> availabilityService.addDoctorAvailability(createAvailabilityDto, email));
    }

    @Test
    public void testAddDoctorAvailabilityDoctorHasAlreadyPlannedBreak() {
        when(userService.getDoctorByEmail(email)).thenReturn(user);
        when(availabilityRepository.hasDoctorBrake(user, availabilityDayDto.getDate(), LocalTime.parse(availabilityDayDto.getBrakeTimeStart()), LocalTime.parse(availabilityDayDto.getBrakeTimeEnd()))).thenReturn(true);
        Assertions.assertThrows(AvailabilityAlreadyExistsException.class, () -> availabilityService.addDoctorAvailability(createAvailabilityDto, email));
    }

    @Test
    public void testGetDoctorAvailability() {
        when(userService.getDoctorByEmail(email)).thenReturn(user);
        when(availabilityRepository.findByDoctor(user)).thenReturn(List.of());
        Assertions.assertEquals(0, availabilityService.getDoctorAvailability(email).size());
    }

    @Test
    public void testIsDoctorAvailable() {
        when(availabilityRepository.isDeclared(user, availabilityDayDto.getDate(), LocalTime.parse(availabilityDayDto.getStartTime()), LocalTime.parse(availabilityDayDto.getEndTime()))).thenReturn(true);
        Assertions.assertTrue(availabilityService.isDoctorAvailable(user, availabilityDayDto.getDate(), LocalTime.parse(availabilityDayDto.getStartTime()), LocalTime.parse(availabilityDayDto.getEndTime())));
    }
}
