package com.dentalapp.backend.unit.controllers;

import com.dentalapp.backend.controllers.PrescriptionController;
import com.dentalapp.backend.model.prescription.dtos.CreatePrescriptionDto;
import com.dentalapp.backend.model.prescription.dtos.CreatePrescriptionsDto;
import com.dentalapp.backend.model.prescription.dtos.UpdatePrescriptionDto;
import com.dentalapp.backend.services.AppointmentService;
import com.dentalapp.backend.services.PrescriptionService;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class PrescriptionControllerTests {
    @Mock
    private PrescriptionService prescriptionService;

    @Mock
    private AppointmentService appointmentService;

    @InjectMocks
    private PrescriptionController prescriptionController;

    @Test
    public void testValidation() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        CreatePrescriptionsDto createPrescriptionsDto = new CreatePrescriptionsDto();
        Assertions.assertEquals(2, validator.validate(createPrescriptionsDto).size());
        CreatePrescriptionDto createPrescriptionDto = new CreatePrescriptionDto();
        createPrescriptionsDto.setCreatePrescriptionsDtoList(List.of(createPrescriptionDto));
        Assertions.assertEquals(2, validator.validate(createPrescriptionDto).size());
    }

    @Test
    public void testCreatePrescriptions() {
        CreatePrescriptionsDto createPrescriptionsDto = new CreatePrescriptionsDto();
        CreatePrescriptionDto createPrescriptionDto = new CreatePrescriptionDto();
        createPrescriptionDto.setMedicine("Medicine");
        createPrescriptionDto.setDosage("Dosage");
        createPrescriptionsDto.setCreatePrescriptionsDtoList(List.of(createPrescriptionDto));
        createPrescriptionsDto.setAppointmentId(1L);
        ResponseEntity<String> response = prescriptionController.createPrescriptions(createPrescriptionsDto);
        Assertions.assertEquals(201, response.getStatusCode().value());
        Assertions.assertEquals("Prescriptions added successfully", response.getBody());
    }

    @Test
    public void testUpdatePrescription() {
        UpdatePrescriptionDto updatePrescriptionDto = new UpdatePrescriptionDto();
        updatePrescriptionDto.setDosage("Dosage");
        updatePrescriptionDto.setMedicine("Medicine");
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
