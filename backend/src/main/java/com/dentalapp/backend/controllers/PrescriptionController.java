package com.dentalapp.backend.controllers;

import com.dentalapp.backend.configuration.JwtService;
import com.dentalapp.backend.model.prescription.dtos.CreatePrescriptionDto;
import com.dentalapp.backend.model.prescription.dtos.CreatePrescriptionsDto;
import com.dentalapp.backend.model.prescription.dtos.GetPrescriptionDto;
import com.dentalapp.backend.model.prescription.dtos.UpdatePrescriptionDto;
import com.dentalapp.backend.services.AppointmentService;
import com.dentalapp.backend.services.PrescriptionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {
    private final PrescriptionService prescriptionService;
    private final AppointmentService appointmentService;
    private final JwtService jwtService;

    public PrescriptionController(PrescriptionService prescriptionService, AppointmentService appointmentService, JwtService jwtService) {
        this.prescriptionService = prescriptionService;
        this.appointmentService = appointmentService;
        this.jwtService = jwtService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<GetPrescriptionDto>> getAllPrescriptions() {
        return ResponseEntity.ok(prescriptionService.findAll());
    }

    @GetMapping("/patient")
    public ResponseEntity<List<GetPrescriptionDto>> getAllPrescriptionsByPatient(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(prescriptionService.findAllByPatient(jwtService.extractEmail(token.substring(7))));
    }

    @GetMapping("/doctor")
    public ResponseEntity<List<GetPrescriptionDto>> getAllPrescriptionsByDoctor(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(prescriptionService.findAllByDoctor(jwtService.extractEmail(token.substring(7))));
    }

    @PostMapping("/add")
    public ResponseEntity<String> createPrescriptions(@Valid @RequestBody CreatePrescriptionDto createPrescriptionsDto) {
        appointmentService.addPrescriptionsToAppointment(createPrescriptionsDto.getAppointmentId(), createPrescriptionsDto);
        return ResponseEntity.status(201).body("Prescriptions added successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePrescription(@PathVariable Long id, @RequestBody UpdatePrescriptionDto updatePrescriptionDto) {
        prescriptionService.updatePrescription(id, updatePrescriptionDto);
        return ResponseEntity.status(200).body("Prescription updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePrescription(@PathVariable Long id) {
        prescriptionService.deletePrescription(id);
        return ResponseEntity.status(200).body("Prescription deleted successfully");
    }
}
