package com.dentalapp.backend.model.appointment.repository;

import com.dentalapp.backend.model.appointment.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate = ?1 AND a.isCancelled = false")
    List<Appointment> findAllByDate(LocalDate date);

    @Query("SELECT a FROM Appointment a WHERE a.patient.userId = ?1 AND a.isCancelled = false")
    List<Appointment> findAllByPatientId(Long patientId);

    @Query("SELECT a FROM Appointment a WHERE a.doctor.userId = ?1 AND a.isCancelled = false")
    List<Appointment> findAllByDoctorId(Long doctorId);

    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate = ?1 AND a.isCancelled = false")
    List<Appointment> findByDate(LocalDate date);

    @Query("SELECT a FROM Appointment a WHERE a.doctor.userId = ?1 AND a.appointmentDate = ?2 AND a.isCancelled = false")
    List<Appointment> findAllByDoctorIdAndDate(Long doctorId, LocalDate date);

    @Query("SELECT a FROM Appointment a WHERE a.isConfirmed = false AND FUNCTION('timestampdiff', DAY, CURRENT_DATE, a.appointmentDate) <= 1")
    List<Appointment> findUnconfirmedAppointments();
}
