package com.dentalapp.backend.model.file.repository;

import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.file.entity.File;
import com.dentalapp.backend.model.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    Optional<File> findByFileName(String fileName);

    Page<File> findFilesByAppointment(Appointment appointment, Pageable pageable);

    @Query("SELECT f FROM File f WHERE f.appointment.patient = :user")
    Page<File> findFilesByUser(User user, Pageable pageable);
}
