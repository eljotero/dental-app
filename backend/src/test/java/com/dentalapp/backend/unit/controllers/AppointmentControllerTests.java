package com.dentalapp.backend.unit.controllers;

import com.dentalapp.backend.configuration.JwtService;
import com.dentalapp.backend.controllers.AppointmentController;
import com.dentalapp.backend.model.appointment.dtos.*;
import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.file.entity.File;
import com.dentalapp.backend.model.invoice.entity.Invoice;
import com.dentalapp.backend.model.prescription.entity.Prescription;
import com.dentalapp.backend.model.referral.entity.Referral;
import com.dentalapp.backend.model.user.entity.User;
import com.dentalapp.backend.services.AppointmentService;
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
import java.util.Objects;

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

    private Invoice invoice;

    private Validator validator;

    private Prescription prescription;

    private Referral referral;

    private File file;

    String token;

    String email;

    @BeforeEach
    public void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }

        User doctor = new User();
        doctor.setUserId(1L);

        User patient = new User();
        patient.setUserId(2L);

        invoice = new Invoice();

        prescription = new Prescription();

        referral = new Referral();

        file = new File();

        token = "Bearer test@mail.com";
        email = "test@mail.com";

        appointment = new Appointment();
        appointment.setAppointmentId(1L);
        appointment.setAppointmentDate(LocalDate.of(2021, 1, 1));
        appointment.setAppointmentStartTime(LocalTime.of(12, 0));
        appointment.setAppointmentEndTime(LocalTime.of(13, 0));
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setInvoice(invoice);
        appointment.setPrescriptions(List.of(prescription));
        appointment.setReferrals(List.of(referral));

        appointment.setFiles(List.of(file));
    }

    @Test
    public void testValidation() {
        CreateAppointmentDto createAppointmentDto = new CreateAppointmentDto();
        createAppointmentDto.setDoctorId(null);
        createAppointmentDto.setAppointmentDate(null);
        createAppointmentDto.setAppointmentStartTime(null);
        createAppointmentDto.setAppointmentEndTime(null);
        Assertions.assertEquals(4, validator.validate(createAppointmentDto).size());
        createAppointmentDto.setAppointmentStartTime("123123");
        createAppointmentDto.setAppointmentEndTime("123123");
        Assertions.assertEquals(4, validator.validate(createAppointmentDto).size());

        UpdateAppointmentDto updateAppointmentDto = new UpdateAppointmentDto();
        updateAppointmentDto.setDoctorId(null);
        updateAppointmentDto.setAppointmentDate(LocalDate.of(2020, 1, 1));
        updateAppointmentDto.setAppointmentStartTime("123123123");
        updateAppointmentDto.setAppointmentEndTime("123123123");
        Assertions.assertEquals(3, validator.validate(updateAppointmentDto).size());
    }

    @Test
    public void testGetAppointments() {
        when(appointmentService.getAppointments(null)).thenReturn(List.of(appointment));
        ResponseEntity<List<Appointment>> response = appointmentController.getAppointments(null);
        verify(appointmentService).getAppointments(null);
        Assertions.assertEquals(List.of(appointment), response.getBody());
        Assertions.assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testGetAppointmentById() {
        GetAppointmentDtoV2 getAppointmentDtoV2 = AppointmentMapper.toGetAppointmentDtoV2(appointment);
        when(appointmentService.getAppointmentByIdDto(1L)).thenReturn(getAppointmentDtoV2);
        ResponseEntity<GetAppointmentDtoV2> response = appointmentController.getAppointmentById(1L);
        verify(appointmentService).getAppointmentByIdDto(1L);
        Assertions.assertEquals(getAppointmentDtoV2, response.getBody());
        Assertions.assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testGetPatientAppointments() {
        GetAppointmentDto getAppointmentDto = AppointmentMapper.toGetAppointmentDto(appointment);
        String patientEmail = "test@mail.com";
        when(jwtService.extractEmail("test")).thenReturn(patientEmail);
        when(appointmentService.getPatientAppointments(patientEmail)).thenReturn(List.of(getAppointmentDto));
        ResponseEntity<List<GetAppointmentDto>> response = appointmentController.getPatientAppointments("Bearer test");
        verify(appointmentService).getPatientAppointments(patientEmail);
        Assertions.assertEquals(List.of(getAppointmentDto), response.getBody());
        Assertions.assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testGetDoctorAppointments() {
        String doctorEmail = "test2@mail.com";
        when(jwtService.extractEmail("test")).thenReturn(doctorEmail);
        when(appointmentService.getDoctorAppointments(doctorEmail, null)).thenReturn(List.of(AppointmentMapper.toGetAppointmentDtoV3(appointment)));
        ResponseEntity<List<GetAppointmentDtoV3>> response = appointmentController.getDoctorAppointments("Bearer test", null);
        verify(appointmentService).getDoctorAppointments(doctorEmail, null);
        GetAppointmentDtoV3 getAppointmentDtoV3 = AppointmentMapper.toGetAppointmentDtoV3(appointment);
        Assertions.assertEquals(getAppointmentDtoV3.getAppointmentId(), Objects.requireNonNull(response.getBody()).getFirst().getAppointmentId());
        Assertions.assertEquals(getAppointmentDtoV3.getAppointmentDate(), response.getBody().getFirst().getAppointmentDate());
        Assertions.assertEquals(getAppointmentDtoV3.getAppointmentStartTime(), response.getBody().getFirst().getAppointmentStartTime());
        Assertions.assertEquals(getAppointmentDtoV3.getAppointmentEndTime(), response.getBody().getFirst().getAppointmentEndTime());
        Assertions.assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testCancelAppointment() {
        ResponseEntity<String> response = appointmentController.cancelAppointment(1L);
        verify(appointmentService).cancelAppointment(1L);
        Assertions.assertEquals(200, response.getStatusCode().value());
        Assertions.assertEquals("Appointment cancelled", response.getBody());
    }

    @Test
    public void testCreateAppointment() {
        CreateAppointmentDto createAppointmentDto = new CreateAppointmentDto();
        createAppointmentDto.setDoctorId(1L);
        createAppointmentDto.setAppointmentDate(LocalDate.of(2021, 1, 1));
        createAppointmentDto.setAppointmentStartTime("12:00");
        createAppointmentDto.setAppointmentEndTime("13:00");
        when(jwtService.extractEmail(token.substring(7))).thenReturn(email);
        ResponseEntity<String> response = appointmentController.createAppointment(token, createAppointmentDto);
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
        updateAppointmentDto.setAppointmentDescription("test");
        when(jwtService.extractEmail(token.substring(7))).thenReturn(email);
        ResponseEntity<String> response = appointmentController.updateAppointment(1L, updateAppointmentDto, token);
        verify(appointmentService).updateAppointment(updateAppointmentDto, 1L, email);
        Assertions.assertEquals(ResponseEntity.ok("Appointment updated"), response);
    }

    @Test
    public void testConfirmAppointment() {
        ResponseEntity<String> response = appointmentController.confirmAppointment(1L);
        verify(appointmentService).confirmAppointment(1L);
        Assertions.assertEquals(ResponseEntity.ok("Appointment confirmed"), response);
    }
}