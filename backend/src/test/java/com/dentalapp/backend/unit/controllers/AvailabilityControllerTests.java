package com.dentalapp.backend.unit.controllers;

import com.dentalapp.backend.configuration.JwtService;
import com.dentalapp.backend.controllers.AvailabilityController;
import com.dentalapp.backend.model.availability.dtos.AvailabilityDayDto;
import com.dentalapp.backend.model.availability.dtos.CreateAvailabilityDto;
import com.dentalapp.backend.model.availability.dtos.GetAvailabilityDto;
import com.dentalapp.backend.services.AvailabilityService;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AvailabilityControllerTests {

    @Mock
    private AvailabilityService availabilityService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AvailabilityController availabilityController;

    private Validator validator;

    private String email;

    private String token;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        email = "test@mail.com";
        token = "Bearer token";
    }

    @Test
    public void testAddDoctorAvailabilityValidation() {
        CreateAvailabilityDto createAvailabilityDto = new CreateAvailabilityDto();
        createAvailabilityDto.setAvailabilityDays(null);
        Assertions.assertEquals(1, validator.validate(createAvailabilityDto).size());
        AvailabilityDayDto availabilityDayDto = new AvailabilityDayDto();
        availabilityDayDto.setDate(null);
        availabilityDayDto.setStartTime(null);
        availabilityDayDto.setEndTime(null);
        createAvailabilityDto.setAvailabilityDays(List.of(availabilityDayDto));
        Assertions.assertEquals(0, validator.validate(createAvailabilityDto).size());
    }

    @Test
    public void testAddDoctorAvailability() {
        CreateAvailabilityDto createAvailabilityDto = new CreateAvailabilityDto();
        AvailabilityDayDto availabilityDayDto = new AvailabilityDayDto();
        availabilityDayDto.setDate(LocalDate.of(2021, 10, 10));
        availabilityDayDto.setStartTime("09:00");
        availabilityDayDto.setEndTime("17:00");
        createAvailabilityDto.setAvailabilityDays(List.of(availabilityDayDto));
        when(jwtService.extractEmail("token")).thenReturn(email);
        ResponseEntity<?> response = availabilityController.addDoctorAvailability(token,createAvailabilityDto);
        verify(availabilityService).addDoctorAvailability(createAvailabilityDto, email);
        Assertions.assertEquals(ResponseEntity.status(201).body("Availability added successfully"), response);
    }

    @Test
    public void testGetDoctorAvailability() {
        GetAvailabilityDto availabilityDto = new GetAvailabilityDto();
        when(jwtService.extractEmail("token")).thenReturn(email);
        when(availabilityService.getDoctorAvailability(email)).thenReturn(List.of(availabilityDto));
        ResponseEntity<?> response = availabilityController.getDoctorAvailability(token);
        verify(availabilityService).getDoctorAvailability(email);
        Assertions.assertEquals(ResponseEntity.ok(List.of(availabilityDto)), response);
    }
}
