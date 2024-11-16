package com.dentalapp.backend.unit.controllers;

import com.dentalapp.backend.configuration.JwtService;
import com.dentalapp.backend.controllers.AppointmentController;
import com.dentalapp.backend.model.appointment.dtos.CreateAppointmentDto;
import com.dentalapp.backend.model.appointment.dtos.UpdateAppointmentDto;
import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.user.dtos.CreateUserDto;
import com.dentalapp.backend.model.user.entity.User;
import com.dentalapp.backend.services.AppointmentService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AppointmentControllerTests {

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AppointmentController appointmentController;

    private Appointment appointment;

    private User doctor;

    private User patient;

    private Validator validator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        doctor = new User();
        doctor.setUserId(1L);

        patient = new User();
        patient.setUserId(2L);

        appointment = new Appointment();
        appointment.setAppointmentId(1L);
        appointment.setAppointmentDate(LocalDateTime.of(2021, 1, 1, 12, 0));
        appointment.setAppointmentDuration(LocalTime.of(1, 0));
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
    }

    @Test
    public void testGetAppointments() {
        when(appointmentService.getAppointments()).thenReturn(List.of(appointment));
        ResponseEntity<?> response = appointmentController.getAppointments();
        verify(appointmentService).getAppointments();
        Assertions.assertEquals(List.of(appointment), response.getBody());
    }

    @Test
    public void testGetAppointmentById() {
        when(appointmentService.getAppointmentById(1L)).thenReturn(appointment);
        ResponseEntity<?> response = appointmentController.getAppointmentById(1L);
        verify(appointmentService).getAppointmentById(1L);
        Assertions.assertEquals(appointment, response.getBody());
    }

    @Test
    public void testGetPatientAppointments() {
        String patientEmail = "test@mail.com";
        when(jwtService.extractEmail("test")).thenReturn(patientEmail);
        when(appointmentService.getPatientAppointments(patientEmail)).thenReturn(List.of(appointment));
        ResponseEntity<?> response = appointmentController.getPatientAppointments("Bearer test");
        verify(appointmentService).getPatientAppointments(patientEmail);
        Assertions.assertEquals(List.of(appointment), response.getBody());
    }

    @Test
    public void testGetDoctorAppointments() {
        String doctorEmail = "test2@mail.com";
        when(jwtService.extractEmail("test")).thenReturn(doctorEmail);
        when(appointmentService.getDoctorAppointments(doctorEmail)).thenReturn(List.of(appointment));
        ResponseEntity<?> response = appointmentController.getDoctorAppointments("Bearer test");
        verify(appointmentService).getDoctorAppointments(doctorEmail);
        Assertions.assertEquals(List.of(appointment), response.getBody());
    }

    @Test
    public void testGetDoctorAppointmentsByDate() {
        String doctorEmail = "test2@mail.com";
        String date = "2021-01-01";
        when(jwtService.extractEmail("test")).thenReturn(doctorEmail);
        when(appointmentService.getDoctorAppointmentsByDate(doctorEmail, date)).thenReturn(List.of(appointment));
        ResponseEntity<?> response = appointmentController.getDoctorAppointmentsByDate("Bearer test", date);
        verify(appointmentService).getDoctorAppointmentsByDate(doctorEmail, date);
        Assertions.assertEquals(List.of(appointment), response.getBody());
    }

    @Test
    public void testGetAppointmentsByDate() {
        String date = "2021-01-01";
        when(appointmentService.getAppointmentsByDate(date)).thenReturn(List.of(appointment));
        ResponseEntity<?> response = appointmentController.getAppointmentsByDate(date);
        verify(appointmentService).getAppointmentsByDate(date);
        Assertions.assertEquals(List.of(appointment), response.getBody());
    }

    @Test
    public void testCancelAppointment() {
        ResponseEntity<?> response = appointmentController.cancelAppointment(1L);
        verify(appointmentService).cancelAppointment(1L);
        Assertions.assertEquals(ResponseEntity.ok("Appointment cancelled"), response);
    }

    @Test
    public void testCreateAppointment() {
        CreateAppointmentDto createAppointmentDto = new CreateAppointmentDto();
        createAppointmentDto.setDoctorId(1L);
        createAppointmentDto.setPatientId(2L);
        createAppointmentDto.setAppointmentDate(LocalDateTime.of(2021, 1, 1, 12, 0));
        createAppointmentDto.setAppointmentDuration(LocalTime.of(1, 0));
        when(appointmentService.createAppointment(createAppointmentDto)).thenReturn(appointment);
        ResponseEntity<?> response = appointmentController.createAppointment(createAppointmentDto);
        verify(appointmentService).createAppointment(createAppointmentDto);
        Assertions.assertEquals(ResponseEntity.ok(appointment), response);
    }

    @Test
    public void testUpdateAppointment() {
        UpdateAppointmentDto updateAppointmentDto = new UpdateAppointmentDto();
        updateAppointmentDto.setDoctorId(1L);
        updateAppointmentDto.setAppointmentDate(LocalDateTime.of(2021, 1, 1, 12, 0));
        updateAppointmentDto.setDescription("test");
        ResponseEntity<?> response = appointmentController.updateAppointment(1L, updateAppointmentDto);
        verify(appointmentService).updateAppointment(updateAppointmentDto, 1L);
        Assertions.assertEquals(ResponseEntity.ok("Appointment updated"), response);
    }

    @Test
    public void testCreateAppointmentValidation() {
        CreateAppointmentDto createAppointmentDto = new CreateAppointmentDto();
        createAppointmentDto.setPatientId(null);
        createAppointmentDto.setDoctorId(null);
        createAppointmentDto.setAppointmentDate(null);
        createAppointmentDto.setAppointmentDuration(null);
        Set<ConstraintViolation<CreateAppointmentDto>> violations = validator.validate(createAppointmentDto);
        Assertions.assertEquals(4, violations.size());
        createAppointmentDto.setAppointmentDate(LocalDateTime.of(2024, 12, 1, 12, 0));
        violations = validator.validate(createAppointmentDto);
        Assertions.assertEquals(4, violations.size());
    }


}
