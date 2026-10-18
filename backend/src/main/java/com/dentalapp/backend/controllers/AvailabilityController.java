package com.dentalapp.backend.controllers;

import com.dentalapp.backend.model.availability.dtos.CreateAvailabilityDto;
import com.dentalapp.backend.model.availability.dtos.GetAvailabilityDto;
import com.dentalapp.backend.model.availability.dtos.UpdateAvailabilityDto;
import com.dentalapp.backend.model.availability.dtos.AvailabilityPageResponse;
import com.dentalapp.backend.model.availability.dtos.AvailabilityQueryParams;
import com.dentalapp.backend.model.availability.dtos.DoctorAvailabilityQueryParams;
import com.dentalapp.backend.model.user.entity.User;
import com.dentalapp.backend.services.AvailabilityService;
import com.dentalapp.backend.services.AvailableSlotsService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/availability")
@AllArgsConstructor
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    private final AvailableSlotsService availableSlotsService;

    @GetMapping
    public ResponseEntity<AvailabilityPageResponse<GetAvailabilityDto>> getAllDoctorAvailability(@Valid @ModelAttribute AvailabilityQueryParams params) {
        return ResponseEntity.ok(availabilityService.getAllDoctorsAvailability(params));
    }

    @GetMapping("/doctor")
    public ResponseEntity<AvailabilityPageResponse<GetAvailabilityDto>> getDoctorAvailability(@AuthenticationPrincipal User user, @Valid @ModelAttribute DoctorAvailabilityQueryParams params) {
        return ResponseEntity.ok(availabilityService.getDoctorAvailability(user.getUserId(), params));
    }

    @PostMapping("/add")
    public ResponseEntity<String> addDoctorAvailability(@AuthenticationPrincipal User user, @RequestBody @Valid CreateAvailabilityDto createAvailabilityDto) {
        availabilityService.addDoctorAvailability(createAvailabilityDto, user.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body("Availability added successfully");
    }

    @PostMapping("/confirm/{id}")
    public ResponseEntity<String> confirmAvailability(@PathVariable Long id) {
        availabilityService.confirmAvailability(id);
        return ResponseEntity.ok("Availability confirmed successfully");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateAvailability(@PathVariable Long id, @RequestBody @Valid UpdateAvailabilityDto updateAvailabilityDto) {
        availabilityService.updateAvailability(id, updateAvailabilityDto);
        return ResponseEntity.ok("Availability updated successfully");
    }

    @GetMapping("/slots/{doctorId}")
    public ResponseEntity<Map<LocalDate, Map<String, String>>> getAvailableSlots(@PathVariable Long doctorId, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(availableSlotsService.getAvailableSlots(doctorId, startDate, endDate));
    }
}
