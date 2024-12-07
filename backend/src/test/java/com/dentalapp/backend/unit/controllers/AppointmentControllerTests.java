package com.dentalapp.backend.unit.controllers;

import com.dentalapp.backend.configuration.JwtService;
import com.dentalapp.backend.controllers.AppointmentController;
import com.dentalapp.backend.model.appointment.dtos.CreateAppointmentDto;
import com.dentalapp.backend.model.appointment.dtos.UpdateAppointmentDto;
import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.user.entity.User;
import com.dentalapp.backend.services.AppointmentService;
import jakarta.validation.ConstraintViolation;
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
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AppointmentControllerTests {

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AppointmentController appointmentController;

    private Appointment appointment;

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        User doctor = new User();
        doctor.setUserId(1L);

        User patient = new User();
        patient.setUserId(2L);

        appointment = new Appointment();
        appointment.setAppointmentId(1L);
        appointment.setAppointmentDate(LocalDate.of(2021, 1, 1));
        appointment.setAppointmentStartTime(LocalTime.of(12, 0));
        appointment.setAppointmentEndTime(LocalTime.of(13, 0));
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
    }

    @Test
    public void testGetAppointments() {
        when(appointmentService.getAppointments()).thenReturn(List.of(appointment));
        ResponseEntity<?> response = appointmentController.getAppointments();
        verify(appointmentService).getAppointments();
        Assertions.assertEquals(List.of(appointment), response.getBody());
        Assertions.assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testGetAppointmentById() {
        when(appointmentService.getAppointmentById(1L)).thenReturn(appointment);
        ResponseEntity<?> response = appointmentController.getAppointmentById(1L);
        verify(appointmentService).getAppointmentById(1L);
        Assertions.assertEquals(appointment, response.getBody());
        Assertions.assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testGetPatientAppointments() {
        String patientEmail = "test@mail.com";
        when(jwtService.extractEmail("test")).thenReturn(patientEmail);
        when(appointmentService.getPatientAppointments(patientEmail)).thenReturn(List.of(appointment));
        ResponseEntity<?> response = appointmentController.getPatientAppointments("Bearer test");
        verify(appointmentService).getPatientAppointments(patientEmail);
        Assertions.assertEquals(List.of(appointment), response.getBody());
        Assertions.assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testGetDoctorAppointments() {
        String doctorEmail = "test2@mail.com";
        when(jwtService.extractEmail("test")).thenReturn(doctorEmail);
        when(appointmentService.getDoctorAppointments(doctorEmail)).thenReturn(List.of(appointment));
        ResponseEntity<?> response = appointmentController.getDoctorAppointments("Bearer test");
        verify(appointmentService).getDoctorAppointments(doctorEmail);
        Assertions.assertEquals(List.of(appointment), response.getBody());
        Assertions.assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testGetDoctorAppointmentsByDate() {
        String doctorEmail = "test2@mail.com";
        LocalDate date = LocalDate.parse("2021-01-01");
        when(jwtService.extractEmail("test")).thenReturn(doctorEmail);
        when(appointmentService.getDoctorAppointmentsByDate(doctorEmail, date)).thenReturn(List.of(appointment));
        ResponseEntity<?> response = appointmentController.getDoctorAppointmentsByDate("Bearer test", date);
        verify(appointmentService).getDoctorAppointmentsByDate(doctorEmail, date);
        Assertions.assertEquals(List.of(appointment), response.getBody());
        Assertions.assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testGetAppointmentsByDate() {
        LocalDate date = LocalDate.parse("2021-01-01");
        when(appointmentService.getAppointmentsByDate(date)).thenReturn(List.of(appointment));
        ResponseEntity<?> response = appointmentController.getAppointmentsByDate(date);
        verify(appointmentService).getAppointmentsByDate(date);
        Assertions.assertEquals(List.of(appointment), response.getBody());
        Assertions.assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testCancelAppointment() {
        ResponseEntity<?> response = appointmentController.cancelAppointment(1L);
        verify(appointmentService).cancelAppointment(1L);
        Assertions.assertEquals(200, response.getStatusCode().value());
        Assertions.assertEquals("Appointment cancelled", response.getBody());
    }

    @Test
    public void testCreateAppointment() {
        String token = "Bearer test@mail.com";
        String email = "test@mail.com";
        CreateAppointmentDto createAppointmentDto = new CreateAppointmentDto();
        createAppointmentDto.setDoctorId(1L);
        createAppointmentDto.setAppointmentDate(LocalDate.of(2021, 1, 1));
        createAppointmentDto.setAppointmentStartTime("12:00");
        createAppointmentDto.setAppointmentEndTime("13:00");
        when(jwtService.extractEmail(token.substring(7))).thenReturn(email);
        ResponseEntity<?> response = appointmentController.createAppointment(token, createAppointmentDto);
        verify(appointmentService).createAppointment(createAppointmentDto, email);
        Assertions.assertEquals(201, response.getStatusCode().value());
        Assertions.assertEquals("Appointment created", response.getBody());
    }

    @Test
    public void testUpdateAppointment() {
        UpdateAppointmentDto updateAppointmentDto = new UpdateAppointmentDto();
        updateAppointmentDto.setDoctorId(1L);
        updateAppointmentDto.setAppointmentDate(LocalDate.of(2021, 1, 1));
        updateAppointmentDto.setAppointmentStartTime("12:00");
        updateAppointmentDto.setAppointmentEndTime("13:00");
        updateAppointmentDto.setDescription("test");
        ResponseEntity<?> response = appointmentController.updateAppointment(1L, updateAppointmentDto);
        verify(appointmentService).updateAppointment(updateAppointmentDto, 1L);
        Assertions.assertEquals(ResponseEntity.ok("Appointment updated"), response);
    }

    @Test
    public void testCreateAppointmentValidation() {
        CreateAppointmentDto createAppointmentDto = new CreateAppointmentDto();
        createAppointmentDto.setDoctorId(null);
        createAppointmentDto.setAppointmentDate(null);
        createAppointmentDto.setAppointmentStartTime(null);
        createAppointmentDto.setAppointmentEndTime(null);
        Set<ConstraintViolation<CreateAppointmentDto>> violations = validator.validate(createAppointmentDto);
        Assertions.assertEquals(5, violations.size());
    }

    @Test
    public void testConfirmAppointment() {
        ResponseEntity<?> response = appointmentController.confirmAppointment(1L);
        verify(appointmentService).confirmAppointment(1L);
        Assertions.assertEquals(ResponseEntity.ok("Appointment confirmed"), response);
    }
}