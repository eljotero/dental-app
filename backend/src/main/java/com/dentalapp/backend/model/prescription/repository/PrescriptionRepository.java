package com.dentalapp.backend.model.prescription.repository;

import com.dentalapp.backend.model.prescription.entity.Prescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    @Query("SELECT p FROM Prescription p WHERE p.appointment.patient.email = :email")
    Page<Prescription> findAllByPatient(String email, Pageable pageable);

    @Query("SELECT p FROM Prescription p WHERE p.appointment.doctor.email = :email")
    Page<Prescription> findAllByDoctor(String email, Pageable pageable);
}
