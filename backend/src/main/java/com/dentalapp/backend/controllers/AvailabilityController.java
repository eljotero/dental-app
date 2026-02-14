package com.dentalapp.backend.controllers;

import com.dentalapp.backend.configuration.JwtService;
import com.dentalapp.backend.model.availability.dtos.CreateAvailabilityDto;
import com.dentalapp.backend.model.availability.dtos.GetAvailabilityDto;
import com.dentalapp.backend.model.availability.dtos.UpdateAvailabilityDto;
import com.dentalapp.backend.model.availability.entity.Availability;
import com.dentalapp.backend.services.AvailabilityService;
import com.dentalapp.backend.services.AvailableSlotsService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/availability")
@AllArgsConstructor
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    private final JwtService jwtService;

    private final AvailableSlotsService availableSlotsService;

    @GetMapping("/all")
    public ResponseEntity<List<GetAvailabilityDto>> getAllDoctorAvailability(@RequestParam(required = false) LocalDate date, @RequestParam(required = false) Long id) {
        Specification<Availability> specification = Specification.where(null);
        if (date != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("availabilityDate"), date));
        }
        if (id != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("doctor").get("id"), id));
        }
        return ResponseEntity.ok(availabilityService.getAllDoctorsAvailability(specification));
    }

    @GetMapping("/doctor")
    public ResponseEntity<List<GetAvailabilityDto>> getDoctorAvailability(@RequestHeader("Authorization") String token, @RequestParam(required = false) LocalDate date) {
        Specification<Availability> specification = Specification.where(null);
        specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("doctor").get("email"), jwtService.extractEmail(token.substring(7))));
        if (date != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("availabilityDate"), date));
        }
        return ResponseEntity.ok(availabilityService.getDoctorAvailability(specification));
    }

    @PostMapping("/add")
    public ResponseEntity<String> addDoctorAvailability(@RequestHeader("Authorization") String token, @RequestBody @Valid CreateAvailabilityDto createAvailabilityDto) {
        availabilityService.addDoctorAvailability(createAvailabilityDto, jwtService.extractEmail(token.substring(7)));
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
    public ResponseEntity<Map<LocalDate, Map<String, String>>> getAvailableSlots(
            @PathVariable Long doctorId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(availableSlotsService.getAvailableSlots(doctorId, startDate, endDate));
    }
}
