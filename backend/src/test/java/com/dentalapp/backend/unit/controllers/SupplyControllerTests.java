package com.dentalapp.backend.unit.controllers;

import com.dentalapp.backend.controllers.SupplyController;
import com.dentalapp.backend.model.supplies.dtos.CreateSupplyDto;
import com.dentalapp.backend.model.supplies.dtos.UpdateSupplyDto;
import com.dentalapp.backend.model.supplies.entity.Supply;
import com.dentalapp.backend.services.SupplyService;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SupplyControllerTests {
    @Mock
    private SupplyService supplyService;

    @InjectMocks
    private SupplyController supplyController;

    private Supply supply;

    private Validator validator;

    @BeforeEach
    public void setUp() {
        supply = new Supply();
        supply.setName("Test Supply");
        supply.setQuantity(10.0);
        supply.setLink("https://test.com");

        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    public void testValidation() {
        CreateSupplyDto createSupplyDto = new CreateSupplyDto();
        createSupplyDto.setName("");
        createSupplyDto.setQuantity(-5.0);
        createSupplyDto.setLink("");
        Assertions.assertEquals(2, validator.validate(createSupplyDto).size());

        UpdateSupplyDto updateSupplyDto = new UpdateSupplyDto();
        updateSupplyDto.setQuantity(-5.0);
        Assertions.assertEquals(1, validator.validate(updateSupplyDto).size());
    }

    @Test
    public void testGetAllSupplies() {
        when(supplyService.findAll()).thenReturn(List.of(supply));
        ResponseEntity<List<Supply>> response = supplyController.getAllSupplies();
        Assertions.assertEquals(200, response.getStatusCode().value());
        Assertions.assertEquals(1, Objects.requireNonNull(response.getBody()).size());
        Assertions.assertEquals(List.of(supply), response.getBody());
    }

    @Test
    public void testGetSupplyById() {
        when(supplyService.findById(1L)).thenReturn(supply);
        ResponseEntity<Supply> response = supplyController.getSupplyById(1L);
        Assertions.assertEquals(200, response.getStatusCode().value());
        Assertions.assertEquals(supply, response.getBody());
    }

    @Test
    public void testAddSupply() {
        CreateSupplyDto createSupplyDto = new CreateSupplyDto();
        createSupplyDto.setName("Test Supply");
        createSupplyDto.setQuantity(10.0);
        createSupplyDto.setLink("https://test.com");
        ResponseEntity<String> response = supplyController.addSupply(createSupplyDto);
        Assertions.assertEquals(201, response.getStatusCode().value());
        verify(supplyService).createSupply(createSupplyDto);
    }

    @Test
    public void testUpdateSupply() {
        UpdateSupplyDto updateSupplyDto = new UpdateSupplyDto();
        updateSupplyDto.setName("Test Supply");
        updateSupplyDto.setQuantity(10.0);
        updateSupplyDto.setLink("https://test.com");
        ResponseEntity<String> response = supplyController.updateSupply(1L, updateSupplyDto);
        Assertions.assertEquals(200, response.getStatusCode().value());
        verify(supplyService).updateSupply(1L, updateSupplyDto);
    }

    @Test
    public void testDeleteSupply() {
        ResponseEntity<String> response = supplyController.deleteSupply(1L);
        Assertions.assertEquals(200, response.getStatusCode().value());
        verify(supplyService).removeSupply(1L);
    }
}
