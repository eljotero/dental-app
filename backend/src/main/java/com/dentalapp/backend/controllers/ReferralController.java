package com.dentalapp.backend.controllers;

import com.dentalapp.backend.model.referral.dtos.CreateReferralsDto;
import com.dentalapp.backend.model.referral.dtos.UpdateReferralDto;
import com.dentalapp.backend.model.referral.entity.Referral;
import com.dentalapp.backend.services.ReferralService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/referrals")
public class ReferralController {

    private final ReferralService referralService;

    public ReferralController(ReferralService referralService) {
        this.referralService = referralService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Referral>> getAllReferrals() {
        return ResponseEntity.ok(referralService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Referral> getReferralById(@PathVariable Long id) {
        return ResponseEntity.ok(referralService.findById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<String> createReferral(@Valid @RequestBody CreateReferralsDto createReferralsDto) {
        referralService.createReferral(createReferralsDto);
        return ResponseEntity.status(201).body("Referral created successfully");
    }

    @PutMapping("/{referralId}")
    public ResponseEntity<String> updateReferral(@PathVariable Long referralId, @Valid @RequestBody UpdateReferralDto updateReferralDto) {
        referralService.updateReferral(referralId, updateReferralDto);
        return ResponseEntity.status(200).body("Referral updated successfully");
    }

    @DeleteMapping("/{referralId}")
    public ResponseEntity<String> deleteReferral(@PathVariable Long referralId) {
        referralService.deleteReferral(referralId);
        return ResponseEntity.status(200).body("Referral deleted successfully");
    }
}
