package com.dentalapp.backend.services;

import com.dentalapp.backend.model.prescription.dtos.*;
import com.dentalapp.backend.model.prescription.entity.Prescription;
import com.dentalapp.backend.model.prescription.exceptions.PrescriptionNotFoundException;
import com.dentalapp.backend.model.prescription.repository.PrescriptionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    private final PrescriptionMapper prescriptionMapper;

    public List<GetPrescriptionDto> findAll() {
        return prescriptionRepository.findAll().stream().map(prescriptionMapper::toGetPrescriptionDto).toList();
    }

    public List<GetPrescriptionDto> findAllByPatient(String email) {
        return prescriptionRepository.findAllByPatient(email).stream().map(prescriptionMapper::toGetPrescriptionDto).toList();
    }

    public List<GetPrescriptionDto> findAllByDoctor(String email) {
        return prescriptionRepository.findAllByDoctor(email).stream().map(prescriptionMapper::toGetPrescriptionDto).toList();
    }

    @Transactional
    public void updatePrescription(Long id, UpdatePrescriptionDto updatePrescriptionDto) {
        Prescription prescription = prescriptionRepository.findById(id).orElseThrow(PrescriptionNotFoundException::new);
        Prescription prescriptionDB = prescriptionMapper.toUpdateEntity(prescription, updatePrescriptionDto);
        prescriptionRepository.save(prescriptionDB);
    }

    @Transactional
    public void deletePrescription(Long id) {
        Prescription prescription = prescriptionRepository.findById(id).orElseThrow(PrescriptionNotFoundException::new);
        prescription.onDelete();
        prescriptionRepository.save(prescription);
    }
}
