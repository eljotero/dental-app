package com.dentalapp.backend.model.patient.repostitory;

import com.dentalapp.backend.model.patient.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
