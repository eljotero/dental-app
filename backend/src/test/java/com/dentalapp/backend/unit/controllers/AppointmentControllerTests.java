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

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AppointmentControllerTests {

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private JwtService jwtService;

    @Mock
    private AppointmentMapper appointmentMapper;

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
    void setUp() {
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
    void testValidation() {
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
    void testCancelAppointment() {
        ResponseEntity<String> response = appointmentController.cancelAppointment(1L);
        verify(appointmentService).cancelAppointment(1L);
        Assertions.assertEquals(200, response.getStatusCode().value());
        Assertions.assertEquals("Appointment cancelled", response.getBody());
    }

    @Test
    void testConfirmAppointment() {
        ResponseEntity<String> response = appointmentController.confirmAppointment(1L);
        verify(appointmentService).confirmAppointment(1L);
        Assertions.assertEquals(ResponseEntity.ok("Appointment confirmed"), response);
    }
}
