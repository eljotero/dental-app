package com.dentalapp.backend.model.token.repository;

import com.dentalapp.backend.model.token.entity.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {

    @Query("SELECT c FROM ConfirmationToken c WHERE c.token = ?1")
    Optional<ConfirmationToken> findByToken(String token);
}
