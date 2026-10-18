package com.dentalapp.backend.controllers;

import com.dentalapp.backend.model.referral.dtos.CreateReferralDto;
import com.dentalapp.backend.model.referral.dtos.GetReferralDto;
import com.dentalapp.backend.model.referral.dtos.UpdateReferralDto;
import com.dentalapp.backend.model.referral.dtos.ReferralPageResponse;
import com.dentalapp.backend.model.referral.dtos.ReferralQueryParams;
import com.dentalapp.backend.model.user.entity.User;
import com.dentalapp.backend.services.ReferralService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/referrals")
@AllArgsConstructor
public class ReferralController {

    private final ReferralService referralService;

    @GetMapping("/all")
    public ResponseEntity<ReferralPageResponse<GetReferralDto>> getAllReferrals(@Valid @ModelAttribute ReferralQueryParams params) {
        return ResponseEntity.ok(referralService.findAll(params));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetReferralDto> getReferralById(@PathVariable Long id) {
        return ResponseEntity.ok(referralService.findById(id));
    }

    @GetMapping("/patient")
    public ResponseEntity<ReferralPageResponse<GetReferralDto>> getAllReferralsByPatient(
            @AuthenticationPrincipal User user, @Valid @ModelAttribute ReferralQueryParams params) {
        return ResponseEntity.ok(referralService.findAllByPatient(user.getEmail(), params));
    }

    @GetMapping("/doctor")
    public ResponseEntity<ReferralPageResponse<GetReferralDto>> getAllReferralsByDoctor(
            @AuthenticationPrincipal User user, @Valid @ModelAttribute ReferralQueryParams params) {
        return ResponseEntity.ok(referralService.findAllByDoctor(user.getEmail(), params));
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
