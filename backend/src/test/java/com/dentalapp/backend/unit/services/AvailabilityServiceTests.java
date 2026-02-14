package com.dentalapp.backend.unit.services;

import com.dentalapp.backend.model.availability.dtos.AvailabilityDayDto;
import com.dentalapp.backend.model.availability.dtos.CreateAvailabilityDto;
import com.dentalapp.backend.model.availability.dtos.UpdateAvailabilityDto;
import com.dentalapp.backend.model.availability.entity.Availability;
import com.dentalapp.backend.model.availability.exceptions.AvailabilityAlreadyExistsException;
import com.dentalapp.backend.model.availability.exceptions.AvailabilityNotFoundException;
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
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AvailabilityServiceTests {

    @Mock
    private AvailabilityRepository availabilityRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private AvailabilityService availabilityService;

    private CreateAvailabilityDto createAvailabilityDto;

    private AvailabilityDayDto availabilityDayDto;

    private User user;

    private String email;

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

//    @Test
//    public void testGetAllDoctorsAvailability() {
//        when(availabilityRepository.findAll((Specification<Availability>) any())).thenReturn(List.of());
//        Assertions.assertEquals(0, availabilityService.getAllDoctorsAvailability(null).size());
//    }
//
//    @Test
//    public void testGetDoctorAvailability() {
//        when(availabilityRepository.findAll((Specification<Availability>) any())).thenReturn(List.of());
//        Assertions.assertEquals(0, availabilityService.getDoctorAvailability(null).size());
//    }
//
//    @Test
//    public void testAddDoctorAvailability() {
//        when(userService.getDoctorByEmail(email)).thenReturn(user);
//        availabilityService.addDoctorAvailability(createAvailabilityDto, email);
//    }

    @Test
    public void testAddDoctorAvailabilityDoctorHasAlreadyPlannedBreak() {
        when(userService.getDoctorByEmail(email)).thenReturn(user);
        when(availabilityRepository.hasDoctorAvailability(user, availabilityDayDto.getDate())).thenReturn(true);
        Assertions.assertThrows(AvailabilityAlreadyExistsException.class, () -> availabilityService.addDoctorAvailability(createAvailabilityDto, email));
    }

    @Test
    public void testConfirmAvailability() {
        when(availabilityRepository.findById(1L)).thenReturn(Optional.of(new Availability()));
        availabilityService.confirmAvailability(1L);
    }

    @Test
    public void testConfirmAvailabilityAvailabilityNotFound() {
        when(availabilityRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(AvailabilityNotFoundException.class, () -> availabilityService.confirmAvailability(1L));
    }

//    @Test
//    public void testUpdateAvailability() {
//        UpdateAvailabilityDto updateAvailabilityDto = new UpdateAvailabilityDto();
//        when(availabilityRepository.findById(1L)).thenReturn(Optional.of(new Availability()));
//        availabilityService.updateAvailability(1L, updateAvailabilityDto);
//        verify(availabilityRepository).save(any(Availability.class));
//    }

    @Test
    public void testUpdateAvailabilityAvailabilityNotFound() {
        UpdateAvailabilityDto updateAvailabilityDto = new UpdateAvailabilityDto();
        when(availabilityRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(AvailabilityNotFoundException.class, () -> availabilityService.updateAvailability(1L, updateAvailabilityDto));
    }

    @Test
    public void testIsDoctorAvailable() {
        when(availabilityRepository.isDeclared(user, availabilityDayDto.getDate(), LocalTime.parse(availabilityDayDto.getStartTime()), LocalTime.parse(availabilityDayDto.getEndTime()))).thenReturn(true);
        Assertions.assertTrue(availabilityService.isDoctorAvailable(user, availabilityDayDto.getDate(), LocalTime.parse(availabilityDayDto.getStartTime()), LocalTime.parse(availabilityDayDto.getEndTime())));
    }

    @Test
    public void testGetDoctorAvailabilityV2() {
        when(availabilityRepository.findAllByDoctorUserIdAndAvailabilityDate(1L, LocalDate.of(2024, 1, 1))).thenReturn(List.of());
        Assertions.assertEquals(0, availabilityService.getDoctorAvailability(1L, LocalDate.of(2024, 1, 1)).size());
    }
}