package com.dentalapp.backend.model.supplies.repository;

import com.dentalapp.backend.model.supplies.entity.Supply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplyRepository extends JpaRepository<Supply, Long> {
}
