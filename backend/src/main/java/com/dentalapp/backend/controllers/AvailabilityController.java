package com.dentalapp.backend.controllers;

import com.dentalapp.backend.configuration.JwtService;
import com.dentalapp.backend.model.availability.dtos.CreateAvailabilityDto;
import com.dentalapp.backend.model.availability.dtos.GetAvailabilityDto;
import com.dentalapp.backend.services.AvailabilityService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/availability")
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    private final JwtService jwtService;

    public AvailabilityController(AvailabilityService availabilityService, JwtService jwtService) {
        this.availabilityService = availabilityService;
        this.jwtService = jwtService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addDoctorAvailability(@RequestHeader("Authorization") String token, @RequestBody @Valid CreateAvailabilityDto createAvailabilityDto) {
        availabilityService.addDoctorAvailability(createAvailabilityDto, jwtService.extractEmail(token.substring(7)));
        return ResponseEntity.status(HttpStatus.CREATED).body("Availability added successfully");
    }

    @GetMapping("/get")
    public ResponseEntity<List<GetAvailabilityDto>> getDoctorAvailability(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(availabilityService.getDoctorAvailability(jwtService.extractEmail(token.substring(7))));
    }
}
