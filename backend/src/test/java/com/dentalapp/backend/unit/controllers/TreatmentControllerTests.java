package com.dentalapp.backend.unit.controllers;

import com.dentalapp.backend.controllers.TreatmentController;
import com.dentalapp.backend.model.treatment.dtos.CreateTreatmentDto;
import com.dentalapp.backend.model.treatment.dtos.UpdateTreatmentDto;
import com.dentalapp.backend.model.treatment.entity.Treatment;
import com.dentalapp.backend.services.TreatmentService;
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

    @BeforeEach
    public void setUp() {
        treatment = new Treatment();
        treatment.setTreatmentId(1L);
        treatment.setTreatmentName("Treatment 1");
        treatment.setTreatmentDescription("Treatment 1 Description");
        treatment.setTreatmentPrice(100L);

        createTreatmentDto = new CreateTreatmentDto();
        createTreatmentDto.setName("Treatment 2");
        createTreatmentDto.setPrice(100L);
        createTreatmentDto.setDescription("Treatment 2 Description");

        updateTreatmentDto = new UpdateTreatmentDto();
        updateTreatmentDto.setName("Treatment 3");
        updateTreatmentDto.setPrice(100L);
        updateTreatmentDto.setDescription("Treatment 3 Description");
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
    }

    @Test
    public void testUpdateTreatment() {
        ResponseEntity<String> response = treatmentController.updateTreatment(1L, updateTreatmentDto);
        verify(treatmentService).updateTreatment(1L, updateTreatmentDto);
        Assertions.assertEquals("Treatment updated successfully", response.getBody());
    }

    @Test
    public void testDeleteTreatment() {
        ResponseEntity<?> response = treatmentController.deleteTreatment(1L);
        verify(treatmentService).deleteTreatment(1L);
        Assertions.assertEquals("Treatment deleted successfully", response.getBody());
    }
}
