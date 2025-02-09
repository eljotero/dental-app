package com.dentalapp.backend.unit.controllers;

import com.dentalapp.backend.configuration.JwtService;
import com.dentalapp.backend.controllers.AvailabilityController;
import com.dentalapp.backend.model.availability.dtos.AvailabilityDayDto;
import com.dentalapp.backend.model.availability.dtos.GetAvailabilityDto;
import com.dentalapp.backend.model.availability.dtos.UpdateAvailabilityDto;
import com.dentalapp.backend.model.availability.entity.Availability;
import com.dentalapp.backend.services.AvailabilityService;
import com.dentalapp.backend.services.AvailableSlotsService;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AvailabilityControllerTests {

    @Mock
    private AvailabilityService availabilityService;

    @Mock
    private JwtService jwtService;

    @Mock
    private AvailableSlotsService availableSlotsService;

    @InjectMocks
    private AvailabilityController availabilityController;

    private Validator validator;

    private String email;

    private String token;

    private AvailabilityDayDto createAvailabilityDto;

    private UpdateAvailabilityDto updateAvailabilityDto;


    @BeforeEach
    public void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
        email = "test@mail.com";
        token = "Bearer token";

        createAvailabilityDto = new AvailabilityDayDto();
        createAvailabilityDto.setDate(LocalDate.of(2021, 10, 10));
        createAvailabilityDto.setStartTime("09:00");
        createAvailabilityDto.setEndTime("17:00");

        updateAvailabilityDto = new UpdateAvailabilityDto();
        updateAvailabilityDto.setStartTime("09:00");
        updateAvailabilityDto.setEndTime("17:00");
        updateAvailabilityDto.setBrakeTimeStart("12:00");
        updateAvailabilityDto.setBrakeTimeEnd("13:00");
    }

    @Test
    public void testGetAllDoctorAvailability() {
        LocalDate date = LocalDate.of(2021, 10, 10);
        Long id = 1L;
        List<GetAvailabilityDto> availabilityList = List.of(new GetAvailabilityDto());

        Specification<Availability> specification = Specification.where(null);
        specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("availabilityDate"), date))
                .and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("doctor").get("id"), id));

        when(availabilityService.getAllDoctorsAvailability(any(Specification.class))).thenReturn(availabilityList);

        ResponseEntity<List<GetAvailabilityDto>> response = availabilityController.getAllDoctorAvailability(date, id);

        verify(availabilityService, times(1)).getAllDoctorsAvailability(any(Specification.class));
        Assertions.assertEquals(ResponseEntity.ok(availabilityList), response);
    }

    @Test
    public void testGetDoctorAvailability() {
        LocalDate date = LocalDate.of(2021, 10, 10);
        String token = "Bearer token";
        List<GetAvailabilityDto> availabilityList = List.of(new GetAvailabilityDto());

        Specification<Availability> specification = Specification.where(null);
        specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("doctor").get("email"), "doctor@example.com"))
                .and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("availabilityDate"), date));

        when(availabilityService.getDoctorAvailability(any(Specification.class))).thenReturn(availabilityList);

        ResponseEntity<List<GetAvailabilityDto>> response = availabilityController.getDoctorAvailability(token, date);

        verify(availabilityService, times(1)).getDoctorAvailability(any(Specification.class));
        Assertions.assertEquals(ResponseEntity.ok(availabilityList), response);
    }

    @Test
    public void testValidation() {

        createAvailabilityDto.setDate(null);
        createAvailabilityDto.setStartTime(null);
        createAvailabilityDto.setEndTime(null);
        Assertions.assertEquals(3, validator.validate(createAvailabilityDto).size());

        updateAvailabilityDto.setStartTime("10:00");
        updateAvailabilityDto.setEndTime("09:00");
        Assertions.assertEquals(1, validator.validate(updateAvailabilityDto).size());
    }

    @Test
    public void testAddDoctorAvailability() {
        when(jwtService.extractEmail("token")).thenReturn(email);
        ResponseEntity<String> response = availabilityController.addDoctorAvailability(token, createAvailabilityDto);
        verify(availabilityService).addDoctorAvailability(createAvailabilityDto, email);
        Assertions.assertEquals(ResponseEntity.status(201).body("Availability added successfully"), response);
    }

    @Test
    public void testUpdateAvailabilityValidation() {
        ResponseEntity<String> response = availabilityController.updateAvailability(1L, updateAvailabilityDto);
        verify(availabilityService).updateAvailability(1L, updateAvailabilityDto);
        Assertions.assertEquals(ResponseEntity.ok("Availability updated successfully"), response);
        Assertions.assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testGetAvailableSlots() {
        Long doctorId = 1L;
        LocalDate startDate = LocalDate.of(2021, 10, 10);
        LocalDate endDate = LocalDate.of(2021, 10, 15);
        Map<LocalDate, Map<String, String>> availableSlots = Map.of(LocalDate.of(2021, 10, 10), new LinkedHashMap<>());
        when(availableSlotsService.getAvailableSlots(doctorId, startDate, endDate)).thenReturn(availableSlots);
        ResponseEntity<Map<LocalDate, Map<String, String>>> response = availabilityController.getAvailableSlots(doctorId, startDate, endDate);
        verify(availableSlotsService).getAvailableSlots(doctorId, startDate, endDate);
        Assertions.assertEquals(ResponseEntity.ok(availableSlots), response);
        Assertions.assertEquals(200, response.getStatusCode().value());
    }
}
