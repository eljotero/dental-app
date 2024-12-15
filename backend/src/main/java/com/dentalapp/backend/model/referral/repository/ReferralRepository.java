package com.dentalapp.backend.model.referral.repository;

import com.dentalapp.backend.model.referral.entity.Referral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReferralRepository extends JpaRepository<Referral, Long> {
    @Query("SELECT r FROM Referral r WHERE r.appointment.patient.email = ?1")
    List<Referral> findAllByPatientEmail(String patientEmail);

    @Query("SELECT r FROM Referral r WHERE r.appointment.doctor.email = ?1")
    List<Referral> findAllByDoctorEmail(String doctorEmail);
}
