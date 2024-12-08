package com.dentalapp.backend.model.prescription.repository;

import com.dentalapp.backend.model.prescription.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
}
