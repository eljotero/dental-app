package com.dentalapp.backend.services;

import com.dentalapp.backend.model.treatment.dtos.CreateTreatmentDto;
import com.dentalapp.backend.model.treatment.dtos.TreatmentMapper;
import com.dentalapp.backend.model.treatment.dtos.UpdateTreatmentDto;
import com.dentalapp.backend.model.treatment.entity.Treatment;
import com.dentalapp.backend.model.treatment.exceptions.TreatmentNotFoundException;
import com.dentalapp.backend.model.treatment.repository.TreatmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TreatmentService {

    private final TreatmentRepository treatmentRepository;

    public TreatmentService(TreatmentRepository treatmentRepository) {
        this.treatmentRepository = treatmentRepository;
    }

    public Treatment getTreatmentById(Long id) {
        return treatmentRepository.findById(id).orElseThrow(() -> new TreatmentNotFoundException("Treatment with id " + id + " not found"));
    }

    public Treatment getTreatmentByName(String name) {
        return treatmentRepository.findByName(name).orElseThrow(() -> new TreatmentNotFoundException("Treatment with name " + name + " not found"));
    }

    public List<Treatment> getTreatments() {
        return treatmentRepository.findAll();
    }

    @Transactional
    public void addTreatment(CreateTreatmentDto createTreatmentDto) {
        getTreatmentByName(createTreatmentDto.getName());
        Treatment treatment = TreatmentMapper.toEntityCreate(createTreatmentDto);
        treatmentRepository.save(treatment);
    }

    @Transactional
    public void updateTreatment(Long id, UpdateTreatmentDto updateTreatmentDto) {
        Treatment treatment = getTreatmentById(id);
        Treatment dbTreatment = TreatmentMapper.toEntityUpdate(updateTreatmentDto, treatment);
        treatmentRepository.save(dbTreatment);
    }

    @Transactional
    public void deleteTreatment(Long id) {
        Treatment treatment = getTreatmentById(id);
        treatment.setIsActive(false);
        treatmentRepository.save(treatment);
    }
}
