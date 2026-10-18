package com.dentalapp.backend.services;

import com.dentalapp.backend.model.treatment.dtos.CreateTreatmentDto;
import com.dentalapp.backend.model.treatment.dtos.TreatmentMapper;
import com.dentalapp.backend.model.treatment.dtos.UpdateTreatmentDto;
import com.dentalapp.backend.model.treatment.dtos.TreatmentPageResponse;
import com.dentalapp.backend.model.treatment.dtos.TreatmentQueryParams;
import com.dentalapp.backend.model.treatment.entity.Treatment;
import com.dentalapp.backend.model.treatment.exceptions.TreatmentAlreadyExistsException;
import com.dentalapp.backend.model.treatment.exceptions.TreatmentNotFoundException;
import com.dentalapp.backend.model.treatment.repository.TreatmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS)
public class TreatmentService {

    private final TreatmentRepository treatmentRepository;

    private final TreatmentMapper treatmentMapper;

    public Treatment getTreatmentById(Long id) {
        return treatmentRepository.findById(id).orElseThrow(() -> TreatmentNotFoundException.byId(id));
    }

    public Treatment getTreatmentByName(String name) {
        return treatmentRepository.findByName(name).orElseThrow(() -> TreatmentNotFoundException.byName(name));
    }

    public TreatmentPageResponse<Treatment> getAllTreatments(TreatmentQueryParams params) {
        return TreatmentPageResponse.from(treatmentRepository.findAll(params.toPageable()));
    }

    @Transactional
    public void createTreatment(CreateTreatmentDto createTreatmentDto) {
        doesTreatmentExist(createTreatmentDto.getTreatmentName());
        Treatment treatment = treatmentMapper.toEntityCreate(createTreatmentDto);
        treatmentRepository.save(treatment);
    }

    @Transactional
    public void updateTreatment(Long id, UpdateTreatmentDto updateTreatmentDto) {
        Treatment treatment = getTreatmentById(id);
        Treatment dbTreatment = treatmentMapper.toEntityUpdate(treatment, updateTreatmentDto);
        treatmentRepository.save(dbTreatment);
    }

    @Transactional
    public void deleteTreatment(Long id) {
        Treatment treatment = getTreatmentById(id);
        treatmentRepository.delete(treatment);
    }

    private void doesTreatmentExist(String name) {
        treatmentRepository.findByName(name).ifPresent(treatment -> {
            throw new TreatmentAlreadyExistsException(name);
        });
    }
}
