package com.dentalapp.backend.model.prescription.repository;

import com.dentalapp.backend.model.prescription.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    @Query("SELECT p FROM Prescription p WHERE p.appointment.patient.email = :email")
    List<Prescription> findAllByPatient(String email);

    @Query("SELECT p FROM Prescription p WHERE p.appointment.doctor.email = :email")
    List<Prescription> findAllByDoctor(String email);
}
