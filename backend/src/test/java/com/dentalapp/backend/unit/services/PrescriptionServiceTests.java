package com.dentalapp.backend.unit.services;

import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.prescription.dtos.GetPrescriptionDto;
import com.dentalapp.backend.model.prescription.dtos.PrescriptionMapper;
import com.dentalapp.backend.model.prescription.dtos.UpdatePrescriptionDto;
import com.dentalapp.backend.model.prescription.entity.Prescription;
import com.dentalapp.backend.model.prescription.exceptions.PrescriptionNotFoundException;
import com.dentalapp.backend.model.prescription.repository.PrescriptionRepository;
import com.dentalapp.backend.services.PrescriptionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrescriptionServiceTests {

    @Mock
    private PrescriptionRepository prescriptionRepository;

    @Mock
    private PrescriptionMapper prescriptionMapper;

    @InjectMocks
    private PrescriptionService prescriptionService;

    private Prescription prescription;

    @BeforeEach
    void setUp() {
        prescription = new Prescription();
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(1L);
        prescription.setAppointment(appointment);
    }

    @Test
    void testFindAll() {
        when(prescriptionRepository.findAll()).thenReturn(List.of(prescription));

        List<GetPrescriptionDto> result = prescriptionService.findAll();

        verify(prescriptionRepository, times(1)).findAll();
        Assertions.assertEquals(1, result.size());
    }

    @Test
    void testFindAllByPatient() {
        String email = "patient@example.com";
        when(prescriptionRepository.findAllByPatient(email)).thenReturn(List.of(prescription));

        List<GetPrescriptionDto> result = prescriptionService.findAllByPatient(email);

        verify(prescriptionRepository, times(1)).findAllByPatient(email);
        Assertions.assertEquals(1, result.size());
    }

    @Test
    void testFindAllByDoctor() {
        String email = "doctor@example.com";
        when(prescriptionRepository.findAllByDoctor(email)).thenReturn(List.of(prescription));

        List<GetPrescriptionDto> result = prescriptionService.findAllByDoctor(email);

        verify(prescriptionRepository, times(1)).findAllByDoctor(email);
        Assertions.assertEquals(1, result.size());
    }

    @Test
    void testUpdatePrescription() {
        UpdatePrescriptionDto updatePrescriptionDto = new UpdatePrescriptionDto();
        updatePrescriptionDto.setDosage("Dosage");
        updatePrescriptionDto.setMedicineName("Medicine");
        Prescription updatedPrescription = new Prescription();

        when(prescriptionRepository.findById(1L)).thenReturn(Optional.of(prescription));
        when(prescriptionMapper.toUpdateEntity(prescription, updatePrescriptionDto)).thenReturn(updatedPrescription);
        when(prescriptionRepository.save(updatedPrescription)).thenReturn(updatedPrescription);

        prescriptionService.updatePrescription(1L, updatePrescriptionDto);

        verify(prescriptionRepository).save(updatedPrescription);
    }

    @Test
    void testDeletePrescription() {
        when(prescriptionRepository.findById(1L)).thenReturn(Optional.of(prescription));
        prescriptionService.deletePrescription(1L);
        verify(prescriptionRepository, times(1)).delete(prescription);
    }

    @Test
    void testDeletePrescriptionNotFound() {
        when(prescriptionRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(PrescriptionNotFoundException.class, () -> prescriptionService.deletePrescription(1L));
    }
}
