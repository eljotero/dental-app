package com.dentalapp.backend.unit.controllers;

import com.dentalapp.backend.configuration.JwtService;
import com.dentalapp.backend.controllers.PrescriptionController;
import com.dentalapp.backend.model.prescription.dtos.CreatePrescriptionDto;
import com.dentalapp.backend.model.prescription.dtos.CreatePrescriptionsDto;
import com.dentalapp.backend.model.prescription.dtos.GetPrescriptionDto;
import com.dentalapp.backend.model.prescription.dtos.UpdatePrescriptionDto;
import com.dentalapp.backend.services.AppointmentService;
import com.dentalapp.backend.services.PrescriptionService;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PrescriptionControllerTests {
    @Mock
    private PrescriptionService prescriptionService;

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private PrescriptionController prescriptionController;

    private final String token = "Bearer test@mail.com";

    private final String email = "test@mail.com";

    @Test
    public void testValidation() {
        Validator validator;
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
        CreatePrescriptionsDto createPrescriptionsDto = new CreatePrescriptionsDto();
        Assertions.assertEquals(2, validator.validate(createPrescriptionsDto).size());
        CreatePrescriptionDto createPrescriptionDto = new CreatePrescriptionDto();
        createPrescriptionsDto.setCreatePrescriptionsDtoList(List.of(createPrescriptionDto));
        Assertions.assertEquals(3, validator.validate(createPrescriptionDto).size());
    }

    @Test
    public void testGetAllPrescriptions() {
        ResponseEntity<List<GetPrescriptionDto>> response = prescriptionController.getAllPrescriptions();
        Assertions.assertEquals(200, response.getStatusCode().value());
        Assertions.assertEquals(0, Objects.requireNonNull(response.getBody()).size());
    }

    @Test
    public void testGetAllPrescriptionsByPatient() {
        when(jwtService.extractEmail(token.substring(7))).thenReturn(email);
        when(prescriptionService.findAllByPatient(email)).thenReturn(List.of());
        ResponseEntity<List<GetPrescriptionDto>> response = prescriptionController.getAllPrescriptionsByPatient(token);
        Assertions.assertEquals(200, response.getStatusCode().value());
        Assertions.assertEquals(0, Objects.requireNonNull(response.getBody()).size());
    }

    @Test
    public void testGetAllPrescriptionsByDoctor() {
        when(jwtService.extractEmail(token.substring(7))).thenReturn(email);
        when(prescriptionService.findAllByDoctor(email)).thenReturn(List.of());
        ResponseEntity<List<GetPrescriptionDto>> response = prescriptionController.getAllPrescriptionsByDoctor(token);
        Assertions.assertEquals(200, response.getStatusCode().value());
        Assertions.assertEquals(0, Objects.requireNonNull(response.getBody()).size());
    }

    @Test
    public void testCreatePrescriptions() {
        CreatePrescriptionDto createPrescriptionDto = new CreatePrescriptionDto();
        createPrescriptionDto.setMedicineName("Medicine");
        createPrescriptionDto.setDosage("Dosage");
        createPrescriptionDto.setAppointmentId(1L);
        ResponseEntity<String> response = prescriptionController.createPrescriptions(createPrescriptionDto);
        Assertions.assertEquals(201, response.getStatusCode().value());
        Assertions.assertEquals("Prescriptions added successfully", response.getBody());
    }

    @Test
    public void testUpdatePrescription() {
        UpdatePrescriptionDto updatePrescriptionDto = new UpdatePrescriptionDto();
        updatePrescriptionDto.setDosage("Dosage");
        updatePrescriptionDto.setMedicineName("Medicine");
        ResponseEntity<String> response = prescriptionController.updatePrescription(1L, updatePrescriptionDto);
        Assertions.assertEquals(200, response.getStatusCode().value());
        Assertions.assertEquals("Prescription updated successfully", response.getBody());
    }

    @Test
    public void testDeletePrescription() {
        ResponseEntity<String> response = prescriptionController.deletePrescription(1L);
        Assertions.assertEquals(200, response.getStatusCode().value());
        Assertions.assertEquals("Prescription deleted successfully", response.getBody());
    }
}
