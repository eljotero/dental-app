package com.dentalapp.backend.model.referral.repository;

import com.dentalapp.backend.model.referral.entity.Referral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferralRepository extends JpaRepository<Referral, Long> {
}
