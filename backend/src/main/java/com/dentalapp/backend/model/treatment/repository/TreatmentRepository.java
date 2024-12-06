package com.dentalapp.backend.model.treatment.repository;

import com.dentalapp.backend.model.treatment.entity.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TreatmentRepository extends JpaRepository<Treatment, Long> {

    @Query("SELECT t FROM Treatment t WHERE t.id = ?1")
    Optional<Treatment> findById(Long id);

    @Query("SELECT t FROM Treatment t WHERE t.name = ?1")
    Optional<Treatment> findByName(String name);
}
