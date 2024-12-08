package com.dentalapp.backend.unit.services;

import com.dentalapp.backend.model.prescription.dtos.CreatePrescriptionDto;
import com.dentalapp.backend.model.prescription.dtos.CreatePrescriptionsDto;
import com.dentalapp.backend.model.prescription.dtos.UpdatePrescriptionDto;
import com.dentalapp.backend.model.prescription.entity.Prescription;
import com.dentalapp.backend.model.prescription.exceptions.PrescriptionNotFoundException;
import com.dentalapp.backend.model.prescription.repository.PrescriptionRepository;
import com.dentalapp.backend.services.PrescriptionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PrescriptionServiceTests {
    @Mock
    private PrescriptionRepository prescriptionRepository;

    @InjectMocks
    private PrescriptionService prescriptionService;

    @Test
    public void testAddPrescription() {
        CreatePrescriptionsDto createPrescriptionsDto = new CreatePrescriptionsDto();
        CreatePrescriptionDto createPrescriptionDto = new CreatePrescriptionDto();
        createPrescriptionDto.setMedicine("Medicine");
        createPrescriptionDto.setDosage("Dosage");
        createPrescriptionsDto.setCreatePrescriptionsDtoList(List.of(createPrescriptionDto));
        List<Prescription> result = prescriptionService.addPrescriptions(createPrescriptionsDto);
        verify(prescriptionRepository, times(1)).save(any(Prescription.class));
        Assertions.assertEquals(1, result.size());
    }

    @Test
    public void testUpdatePrescription() {
        UpdatePrescriptionDto updatePrescriptionDto = new UpdatePrescriptionDto();
        updatePrescriptionDto.setDosage("Dosage");
        updatePrescriptionDto.setMedicine("Medicine");
        when(prescriptionRepository.findById(1L)).thenReturn(Optional.of(new Prescription()));
        prescriptionService.updatePrescription(1L, updatePrescriptionDto);
        verify(prescriptionRepository, times(1)).save(any(Prescription.class));
    }

    @Test
    public void testDeletePrescription() {
        when(prescriptionRepository.findById(1L)).thenReturn(Optional.of(new Prescription()));
        prescriptionService.deletePrescription(1L);
        verify(prescriptionRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeletePrescriptionNotFound() {
        when(prescriptionRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(PrescriptionNotFoundException.class, () -> prescriptionService.deletePrescription(1L));
    }
}
