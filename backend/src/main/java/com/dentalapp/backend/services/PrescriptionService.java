package com.dentalapp.backend.services;

import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.prescription.dtos.*;
import com.dentalapp.backend.model.prescription.entity.Prescription;
import com.dentalapp.backend.model.prescription.exceptions.PrescriptionNotFoundException;
import com.dentalapp.backend.model.prescription.repository.PrescriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PrescriptionService {
    private final PrescriptionRepository prescriptionRepository;

    public PrescriptionService(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    public List<GetPrescriptionDto> findAll() {
        return prescriptionRepository.findAll().stream().map(PrescriptionMapper::toGetPrescriptionDto).toList();
    }

    public List<GetPrescriptionDto> findAllByPatient(String email) {
        return prescriptionRepository.findAllByPatient(email).stream().map(PrescriptionMapper::toGetPrescriptionDto).toList();
    }

    public List<GetPrescriptionDto> findAllByDoctor(String email) {
        return prescriptionRepository.findAllByDoctor(email).stream().map(PrescriptionMapper::toGetPrescriptionDto).toList();
    }

    @Transactional
    public List<Prescription> addPrescriptions(CreatePrescriptionsDto createPrescriptionsDto, Appointment appointment) {
        List<Prescription> prescriptions = new ArrayList<>();
        for (CreatePrescriptionDto createPrescription : createPrescriptionsDto.getCreatePrescriptionsDtoList()) {
            Prescription prescription = PrescriptionMapper.toEntity(createPrescription, appointment);
            prescriptionRepository.save(prescription);
            prescriptions.add(prescription);
        }
        return prescriptions;
    }

    @Transactional
    public void updatePrescription(Long id, UpdatePrescriptionDto updatePrescriptionDto) {
        Prescription prescription = prescriptionRepository.findById(id).orElseThrow(PrescriptionNotFoundException::new);
        Prescription prescriptionDB = PrescriptionMapper.toUpdateEntity(prescription, updatePrescriptionDto);
        prescriptionRepository.save(prescriptionDB);
    }

    @Transactional
    public void deletePrescription(Long id) {
        if (prescriptionRepository.findById(id).isEmpty()) {
            throw new PrescriptionNotFoundException();
        }
        prescriptionRepository.deleteById(id);
    }
}
