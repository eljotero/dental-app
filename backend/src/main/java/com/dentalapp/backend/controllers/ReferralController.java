package com.dentalapp.backend.controllers;

import com.dentalapp.backend.configuration.JwtService;
import com.dentalapp.backend.model.referral.dtos.CreateReferralDto;
import com.dentalapp.backend.model.referral.dtos.GetReferralDto;
import com.dentalapp.backend.model.referral.dtos.UpdateReferralDto;
import com.dentalapp.backend.services.ReferralService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/referrals")
@AllArgsConstructor
public class ReferralController {

    private final ReferralService referralService;

    private final JwtService jwtService;

    @GetMapping("/all")
    public ResponseEntity<List<GetReferralDto>> getAllReferrals() {
        return ResponseEntity.ok(referralService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetReferralDto> getReferralById(@PathVariable Long id) {
        return ResponseEntity.ok(referralService.findById(id));
    }

    @GetMapping("/patient")
    public ResponseEntity<List<GetReferralDto>> getAllReferralsByPatient(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(referralService.findAllByPatient(jwtService.extractEmail(token.substring(7))));
    }

    @GetMapping("/doctor")
    public ResponseEntity<List<GetReferralDto>> getAllReferralsByDoctor(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(referralService.findAllByDoctor(jwtService.extractEmail(token.substring(7))));
    }

    @PostMapping("/add")
    public ResponseEntity<String> createReferral(@Valid @RequestBody CreateReferralDto createReferralDto) {
        referralService.createReferral(createReferralDto);
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
