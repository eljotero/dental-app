package com.dentalapp.backend.controllers;

import com.dentalapp.backend.configuration.JwtService;
import com.dentalapp.backend.model.availability.dtos.CreateAvailabilityDto;
import com.dentalapp.backend.services.AvailabilityService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController("/availability")
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    private final JwtService jwtService;

    public AvailabilityController(AvailabilityService availabilityService, JwtService jwtService) {
        this.availabilityService = availabilityService;
        this.jwtService = jwtService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addDoctorAvailability(@Valid CreateAvailabilityDto createAvailabilityDto, @RequestHeader("Authorization") String token) {
        availabilityService.addDoctorAvailability(createAvailabilityDto, jwtService.extractEmail(token.substring(7)));
        return ResponseEntity.ok("Availability added successfully");
    }

    @GetMapping("/get")
    public ResponseEntity<?> getDoctorAvailability(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(availabilityService.getDoctorAvailability(jwtService.extractEmail(token.substring(7))));
    }
}
