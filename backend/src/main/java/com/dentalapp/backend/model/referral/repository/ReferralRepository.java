package com.dentalapp.backend.model.referral.repository;

import com.dentalapp.backend.model.referral.entity.Referral;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferralRepository extends JpaRepository<Referral, Long> {
    @Query("SELECT r FROM Referral r WHERE r.appointment.patient.email = ?1")
    Page<Referral> findAllByPatientEmail(String patientEmail, Pageable pageable);

    @Query("SELECT r FROM Referral r WHERE r.appointment.doctor.email = ?1")
    Page<Referral> findAllByDoctorEmail(String doctorEmail, Pageable pageable);
}
