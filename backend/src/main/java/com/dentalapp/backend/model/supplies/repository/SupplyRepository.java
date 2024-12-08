package com.dentalapp.backend.model.supplies.repository;

import com.dentalapp.backend.model.supplies.entity.Supply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SupplyRepository extends JpaRepository<Supply, Long> {
    @Query("SELECT s FROM Supply s WHERE s.name = ?1")
    Optional<Supply> findByName(String name);
}
