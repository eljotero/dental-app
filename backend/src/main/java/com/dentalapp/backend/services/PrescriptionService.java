package com.dentalapp.backend.services;

import com.dentalapp.backend.model.prescription.dtos.*;
import com.dentalapp.backend.model.prescription.entity.Prescription;
import com.dentalapp.backend.model.prescription.exceptions.PrescriptionNotFoundException;
import com.dentalapp.backend.model.prescription.repository.PrescriptionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    private final PrescriptionMapper prescriptionMapper;

    public PrescriptionPageResponse<GetPrescriptionDto> findAll(PrescriptionQueryParams params) {
        return PrescriptionPageResponse.from(prescriptionRepository.findAll(params.toPageable())
                .map(prescriptionMapper::toGetPrescriptionDto));
    }

    public PrescriptionPageResponse<GetPrescriptionDto> findAllByPatient(String email, PrescriptionQueryParams params) {
        return PrescriptionPageResponse.from(prescriptionRepository.findAllByPatient(email, params.toPageable())
                .map(prescriptionMapper::toGetPrescriptionDto));
    }

    public PrescriptionPageResponse<GetPrescriptionDto> findAllByDoctor(String email, PrescriptionQueryParams params) {
        return PrescriptionPageResponse.from(prescriptionRepository.findAllByDoctor(email, params.toPageable())
                .map(prescriptionMapper::toGetPrescriptionDto));
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
        prescriptionRepository.delete(prescription);
    }
}
