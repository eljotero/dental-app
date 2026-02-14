package com.dentalapp.backend.unit.services;

import com.dentalapp.backend.model.treatment.dtos.CreateTreatmentDto;
import com.dentalapp.backend.model.treatment.dtos.UpdateTreatmentDto;
import com.dentalapp.backend.model.treatment.entity.Treatment;
import com.dentalapp.backend.model.treatment.exceptions.TreatmentAlreadyExistsException;
import com.dentalapp.backend.model.treatment.exceptions.TreatmentNotFoundException;
import com.dentalapp.backend.model.treatment.repository.TreatmentRepository;
import com.dentalapp.backend.services.TreatmentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TreatmentServiceTests {

    @Mock
    private TreatmentRepository treatmentRepository;

    @InjectMocks
    private TreatmentService treatmentService;

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
        createTreatmentDto.setTreatmentName("Treatment 2");
        createTreatmentDto.setTreatmentPrice(100L);
        createTreatmentDto.setTreatmentDescription("Treatment 2 Description");

        updateTreatmentDto = new UpdateTreatmentDto();
        updateTreatmentDto.setTreatmentName("Treatment 3");
        updateTreatmentDto.setTreatmentPrice(100L);
        updateTreatmentDto.setTreatmentDescription("Treatment 3 Description");
    }

    @Test
    public void testGetTreatmentById() {
        when(treatmentRepository.findById(1L)).thenReturn(java.util.Optional.of(treatment));
        Treatment treatment = treatmentService.getTreatmentById(1L);
        Assertions.assertEquals(1L, treatment.getTreatmentId());
        Assertions.assertEquals("Treatment 1", treatment.getTreatmentName());
        Assertions.assertEquals("Treatment 1 Description", treatment.getTreatmentDescription());
        Assertions.assertEquals(100L, treatment.getTreatmentPrice());
    }

    @Test
    public void testGetTreatmentByIdNotFound(){
        when(treatmentRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        Assertions.assertThrows(TreatmentNotFoundException.class, () -> treatmentService.getTreatmentById(1L));
    }

    @Test
    public void testGetTreatmentByName() {
        when(treatmentRepository.findByName("Treatment 1")).thenReturn(Optional.of(treatment));
        Treatment treatment = treatmentService.getTreatmentByName("Treatment 1");
        Assertions.assertEquals(1L, treatment.getTreatmentId());
        Assertions.assertEquals("Treatment 1", treatment.getTreatmentName());
        Assertions.assertEquals("Treatment 1 Description", treatment.getTreatmentDescription());
        Assertions.assertEquals(100L, treatment.getTreatmentPrice());
    }

    @Test
    public void testGetTreatmentByNameNotFound() {
        when(treatmentRepository.findByName("Treatment 1")).thenReturn(Optional.empty());
        Assertions.assertThrows(TreatmentNotFoundException.class, () -> treatmentService.getTreatmentByName("Treatment 1"));
    }

    @Test
    public void testGetAllTreatments() {
        when(treatmentRepository.findAll()).thenReturn(List.of(treatment));
        Assertions.assertEquals(1, treatmentService.getAllTreatments().size());
    }

//    @Test
//    public void testCreateTreatment() {
//        when(treatmentRepository.findByName(createTreatmentDto.getTreatmentName())).thenReturn(Optional.empty());
//        treatmentService.createTreatment(createTreatmentDto);
//        verify(treatmentRepository).save(any());
//    }

    @Test
    public void testCreateTreatmentAlreadyExists() {
        when(treatmentRepository.findByName(createTreatmentDto.getTreatmentName())).thenReturn(Optional.of(treatment));
        Assertions.assertThrows(TreatmentAlreadyExistsException.class, () -> treatmentService.createTreatment(createTreatmentDto));
    }

//    @Test
//    public void testUpdateTreatment() {
//        when(treatmentRepository.findById(1L)).thenReturn(Optional.of(treatment));
//        treatmentService.updateTreatment(1L, updateTreatmentDto);
//        verify(treatmentRepository).save(any());
//    }

    @Test
    public void testUpdateTreatmentNotFound() {
        when(treatmentRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(TreatmentNotFoundException.class, () -> treatmentService.updateTreatment(1L, updateTreatmentDto));
    }

    @Test
    public void testDeleteTreatment() {
        when(treatmentRepository.findById(1L)).thenReturn(Optional.of(treatment));
        treatmentService.deleteTreatment(1L);
        verify(treatmentRepository).save(treatment);
    }

    @Test
    public void testDeleteTreatmentNotFound() {
        when(treatmentRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(TreatmentNotFoundException.class, () -> treatmentService.deleteTreatment(1L));
    }
}
