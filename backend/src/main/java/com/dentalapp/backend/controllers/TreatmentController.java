package com.dentalapp.backend.controllers;

import com.dentalapp.backend.model.treatment.dtos.CreateTreatmentDto;
import com.dentalapp.backend.model.treatment.dtos.UpdateTreatmentDto;
import com.dentalapp.backend.model.treatment.entity.Treatment;
import com.dentalapp.backend.services.TreatmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class TreatmentController {

    private final TreatmentService treatmentService;

    public TreatmentController(TreatmentService treatmentService) {
        this.treatmentService = treatmentService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Treatment>> getTreatments() {
        return ResponseEntity.ok(treatmentService.getAllTreatments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Treatment> getTreatmentById(@PathVariable Long id) {
        return ResponseEntity.ok(treatmentService.getTreatmentById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<String> addTreatment(@RequestBody CreateTreatmentDto createTreatmentDto) {
        treatmentService.createTreatment(createTreatmentDto);
        return ResponseEntity.status(201).body("Treatment added successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateTreatment(@PathVariable Long id, @RequestBody UpdateTreatmentDto updateTreatmentDto) {
        treatmentService.updateTreatment(id, updateTreatmentDto);
        return ResponseEntity.ok("Treatment updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTreatment(@PathVariable Long id) {
        treatmentService.deleteTreatment(id);
        return ResponseEntity.ok("Treatment deleted successfully");
    }

}
