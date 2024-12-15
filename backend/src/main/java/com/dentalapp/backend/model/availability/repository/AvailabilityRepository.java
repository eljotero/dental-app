package com.dentalapp.backend.model.availability.repository;

import com.dentalapp.backend.model.availability.entity.Availability;
import com.dentalapp.backend.model.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long>, JpaSpecificationExecutor<Availability> {
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END FROM Availability a WHERE a.doctor = ?1 AND a.availabilityDate = ?2 AND a.availabilityStartTime <= ?3 AND a.availabilityEndTime >= ?4 AND (a.brakeTimeStart IS NULL OR a.brakeTimeStart >= ?4 OR a.brakeTimeEnd <= ?3)")
    boolean isDeclared(User doctor, LocalDate date, LocalTime startTime, LocalTime endTime);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END FROM Availability a WHERE a.doctor = ?1 AND a.availabilityDate = ?2")
    boolean hasDoctorAvailability(User doctor, LocalDate date);

    @Query("SELECT a FROM Availability a WHERE a.doctor = ?1")
    List<Availability> findByDoctor(User doctor);

    @Query("SELECT a FROM Availability a WHERE a.doctor = ?1 AND a.availabilityDate = ?2")
    List<Availability> findByDoctorAndDate(User doctor, LocalDate date);
}
