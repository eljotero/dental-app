package com.dentalapp.backend.model.appointment.repository;

import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>, JpaSpecificationExecutor<Appointment> {

    @Query("SELECT a FROM Appointment a WHERE a.doctor.userId = ?1 AND a.appointmentDate = ?2 AND a.isCancelled = false")
    List<Appointment> findAllByDoctorIdAndDate(Long doctorId, LocalDate date);

    @Query("SELECT a FROM Appointment a WHERE a.isConfirmed = false AND a.appointmentDate <= local date + 1 day")
    List<Appointment> findUnconfirmedAppointments();

    @Query("SELECT a FROM Appointment a WHERE a.doctor = ?1 AND a.appointmentDate = ?2 AND a.isCancelled = false")
    List<Appointment> findAllByDoctorAndDate(User doctor, LocalDate date);
}
