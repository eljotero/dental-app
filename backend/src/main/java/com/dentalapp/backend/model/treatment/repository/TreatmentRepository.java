package com.dentalapp.backend.model.treatment.repository;

import com.dentalapp.backend.model.treatment.entity.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TreatmentRepository extends JpaRepository<Treatment, Long> {
    @Query("SELECT t FROM Treatment t WHERE t.isActive = true")
    List<Treatment> findAll();

    @Query("SELECT t FROM Treatment t WHERE t.treatmentId = ?1 AND t.isActive = true")
    Optional<Treatment> findById(Long id);

    @Query("SELECT t FROM Treatment t WHERE t.treatmentName = ?1 AND t.isActive = true")
    Optional<Treatment> findByName(String name);
}
