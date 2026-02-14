package com.dentalapp.backend.services;

import com.dentalapp.backend.model.treatment.dtos.CreateTreatmentDto;
import com.dentalapp.backend.model.treatment.dtos.TreatmentMapperV2;
import com.dentalapp.backend.model.treatment.dtos.UpdateTreatmentDto;
import com.dentalapp.backend.model.treatment.entity.Treatment;
import com.dentalapp.backend.model.treatment.exceptions.TreatmentAlreadyExistsException;
import com.dentalapp.backend.model.treatment.exceptions.TreatmentNotFoundException;
import com.dentalapp.backend.model.treatment.repository.TreatmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class TreatmentService {

    private final TreatmentRepository treatmentRepository;

    private final TreatmentMapperV2 treatmentMapperV2;

    public Treatment getTreatmentById(Long id) {
        return treatmentRepository.findById(id).orElseThrow(() -> new TreatmentNotFoundException("Treatment with id " + id + " not found"));
    }

    public Treatment getTreatmentByName(String name) {
        return treatmentRepository.findByName(name).orElseThrow(() -> new TreatmentNotFoundException("Treatment with name " + name + " not found"));
    }

    public List<Treatment> getAllTreatments() {
        return treatmentRepository.findAll();
    }

    @Transactional
    public void createTreatment(CreateTreatmentDto createTreatmentDto) {
        doesTreatmentExist(createTreatmentDto.getTreatmentName());
        Treatment treatment = treatmentMapperV2.toEntityCreate(createTreatmentDto);
        treatmentRepository.save(treatment);
    }

    @Transactional
    public void updateTreatment(Long id, UpdateTreatmentDto updateTreatmentDto) {
        Treatment treatment = getTreatmentById(id);
        Treatment dbTreatment = treatmentMapperV2.toEntityUpdate(treatment, updateTreatmentDto);
        treatmentRepository.save(dbTreatment);
    }

    @Transactional
    public void deleteTreatment(Long id) {
        Treatment treatment = getTreatmentById(id);
        treatment.onDelete();
        treatmentRepository.save(treatment);
    }

    private void doesTreatmentExist(String name) {
        treatmentRepository.findByName(name).ifPresent(treatment -> {
            throw new TreatmentAlreadyExistsException("Treatment with name " + name + " already exists");
        });
    }
}
