package com.dentalapp.backend.controllers;

import com.dentalapp.backend.model.supplies.dtos.CreateSupplyDto;
import com.dentalapp.backend.model.supplies.dtos.UpdateSupplyDto;
import com.dentalapp.backend.model.supplies.entity.Supply;
import com.dentalapp.backend.services.SupplyService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supply")
public class SupplyController {

    private final SupplyService supplyService;

    public SupplyController(SupplyService supplyService) {
        this.supplyService = supplyService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Supply>> getAllSupplies() {
        return ResponseEntity.ok(supplyService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Supply> getSupplyById(@PathVariable Long id) {
        return ResponseEntity.ok(supplyService.findById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<String> addSupply(@Valid @RequestBody CreateSupplyDto createSupplyDto) {
        supplyService.createSupply(createSupplyDto);
        return ResponseEntity.status(201).body("Supply added successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateSupply(@PathVariable Long id, @Valid @RequestBody UpdateSupplyDto updateSupplyDto) {
        supplyService.updateSupply(id, updateSupplyDto);
        return ResponseEntity.ok("Supply updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSupply(@PathVariable Long id) {
        supplyService.removeSupply(id);
        return ResponseEntity.ok("Supply deleted successfully");
    }

}
