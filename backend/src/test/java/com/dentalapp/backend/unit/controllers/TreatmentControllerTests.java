package com.dentalapp.backend.unit.controllers;

import com.dentalapp.backend.controllers.TreatmentController;
import com.dentalapp.backend.model.treatment.dtos.CreateTreatmentDto;
import com.dentalapp.backend.model.treatment.dtos.UpdateTreatmentDto;
import com.dentalapp.backend.model.treatment.entity.Treatment;
import com.dentalapp.backend.services.TreatmentService;
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

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TreatmentControllerTests {

    @Mock
    private TreatmentService treatmentService;

    @InjectMocks
    private TreatmentController treatmentController;

    Treatment treatment;

    CreateTreatmentDto createTreatmentDto;

    UpdateTreatmentDto updateTreatmentDto;

    private Validator validator;

    @BeforeEach
    public void setUp() {
        treatment = new Treatment();
        treatment.setTreatmentId(1L);
        treatment.setTreatmentName("Treatment 1");
        treatment.setTreatmentDescription("Treatment 1 Description");
        treatment.setTreatmentPrice(100L);

        createTreatmentDto = new CreateTreatmentDto();
        createTreatmentDto.setTreatmentName("Treatment 2");
        createTreatmentDto.setTreatmentPrice(100L);
        createTreatmentDto.setTreatmentDescription("Treatment 2 Description");

        updateTreatmentDto = new UpdateTreatmentDto();
        updateTreatmentDto.setTreatmentName("Treatment 3");
        updateTreatmentDto.setTreatmentPrice(100L);
        updateTreatmentDto.setTreatmentDescription("Treatment 3 Description");

        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    public void testValidation() {
        createTreatmentDto.setTreatmentName(null);
        createTreatmentDto.setTreatmentDescription(null);
        createTreatmentDto.setTreatmentPrice(-100L);
        Assertions.assertEquals(3, validator.validate(createTreatmentDto).size());
        createTreatmentDto.setTreatmentName("");
        createTreatmentDto.setTreatmentDescription("");
        Assertions.assertEquals(3, validator.validate(createTreatmentDto).size());

        updateTreatmentDto.setTreatmentPrice(-100L);
        Assertions.assertEquals(1, validator.validate(updateTreatmentDto).size());
    }


    @Test
    public void testGetAllTreatments() {
        when(treatmentService.getAllTreatments()).thenReturn(List.of(treatment));
        ResponseEntity<List<Treatment>> response = treatmentController.getTreatments();
        verify(treatmentService).getAllTreatments();
        Assertions.assertEquals(List.of(treatment), response.getBody());
    }

    @Test
    public void testGetTreatmentById() {
        when(treatmentService.getTreatmentById(1L)).thenReturn(treatment);
        ResponseEntity<Treatment> response = treatmentController.getTreatmentById(1L);
        verify(treatmentService).getTreatmentById(1L);
        Assertions.assertEquals(treatment, response.getBody());
    }

    @Test
    public void testCreateTreatment() {
        ResponseEntity<String> response = treatmentController.addTreatment(createTreatmentDto);
        verify(treatmentService).createTreatment(createTreatmentDto);
        Assertions.assertEquals("Treatment added successfully", response.getBody());
        Assertions.assertEquals(201, response.getStatusCode().value());
    }

    @Test
    public void testUpdateTreatment() {
        ResponseEntity<String> response = treatmentController.updateTreatment(1L, updateTreatmentDto);
        verify(treatmentService).updateTreatment(1L, updateTreatmentDto);
        Assertions.assertEquals("Treatment updated successfully", response.getBody());
    }

    @Test
    public void testDeleteTreatment() {
        ResponseEntity<String> response = treatmentController.deleteTreatment(1L);
        verify(treatmentService).deleteTreatment(1L);
        Assertions.assertEquals("Treatment deleted successfully", response.getBody());
    }
}
