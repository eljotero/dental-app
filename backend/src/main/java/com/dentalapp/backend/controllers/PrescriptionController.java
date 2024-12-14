package com.dentalapp.backend.controllers;

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

    public PrescriptionController(PrescriptionService prescriptionService, AppointmentService appointmentService) {
        this.prescriptionService = prescriptionService;
        this.appointmentService = appointmentService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<GetPrescriptionDto>> getAllPrescriptions() {
        return ResponseEntity.ok(prescriptionService.findAll());
    }

    @PostMapping("/add")
    public ResponseEntity<String> createPrescriptions(@Valid @RequestBody CreatePrescriptionsDto createPrescriptionsDto) {
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
