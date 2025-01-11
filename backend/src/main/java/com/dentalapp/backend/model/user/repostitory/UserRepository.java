package com.dentalapp.backend.model.user.repostitory;

import com.dentalapp.backend.model.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.userId = ?1 AND u.userType = 'PATIENT'")
    Optional<User> findPatientById(Long userId);

    @Query("SELECT u FROM User u WHERE u.userId = ?1 AND u.userType = 'DOCTOR'")
    Optional<User> findDoctorById(Long userId);

    @Query("SELECT u FROM User u WHERE u.email = ?1 AND u.userType = 'PATIENT'")
    Optional<User> findPatientByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.email = ?1 AND u.userType = 'DOCTOR'")
    Optional<User> findDoctorByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.userType = 'DOCTOR'")
    List<User> findAllDoctors();
}
