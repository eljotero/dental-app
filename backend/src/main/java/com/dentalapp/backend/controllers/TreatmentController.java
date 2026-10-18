package com.dentalapp.backend.controllers;

import com.dentalapp.backend.model.treatment.dtos.CreateTreatmentDto;
import com.dentalapp.backend.model.treatment.dtos.UpdateTreatmentDto;
import com.dentalapp.backend.model.treatment.dtos.TreatmentPageResponse;
import com.dentalapp.backend.model.treatment.dtos.TreatmentQueryParams;
import com.dentalapp.backend.model.treatment.entity.Treatment;
import com.dentalapp.backend.services.TreatmentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/services")
@AllArgsConstructor
public class TreatmentController {

    private final TreatmentService treatmentService;

    @GetMapping("/all")
    public ResponseEntity<TreatmentPageResponse<Treatment>> getTreatments(@Valid @ModelAttribute TreatmentQueryParams params) {
        return ResponseEntity.ok(treatmentService.getAllTreatments(params));
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
