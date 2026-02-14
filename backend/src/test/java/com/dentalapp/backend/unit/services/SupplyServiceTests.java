package com.dentalapp.backend.unit.services;

import com.dentalapp.backend.model.supplies.dtos.CreateSupplyDto;
import com.dentalapp.backend.model.supplies.dtos.UpdateSupplyDto;
import com.dentalapp.backend.model.supplies.entity.Supply;
import com.dentalapp.backend.model.supplies.exceptions.SupplyAlreadyExistsException;
import com.dentalapp.backend.model.supplies.exceptions.SupplyNotFoundException;
import com.dentalapp.backend.model.supplies.repository.SupplyRepository;
import com.dentalapp.backend.services.SupplyService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SupplyServiceTests {
    @Mock
    private SupplyRepository supplyRepository;

    @InjectMocks
    private SupplyService supplyService;

    private Supply supply;

    private CreateSupplyDto createSupplyDto;

    private UpdateSupplyDto updateSupplyDto;

    @BeforeEach
    public void setUp() {
        supply = new Supply();
        supply.setSupplyId(1L);
        supply.setName("Name");

        createSupplyDto = new CreateSupplyDto();
        createSupplyDto.setName("Name");
        createSupplyDto.setLink("Link");
        createSupplyDto.setQuantity(4.0);

        updateSupplyDto = new UpdateSupplyDto();
        updateSupplyDto.setName("Name updated");
    }

    @Test
    public void testFindAll() {
        when(supplyRepository.findAll()).thenReturn(List.of(supply));
        List<Supply> supplies = supplyService.findAll();
        Assertions.assertEquals(List.of(supply), supplies);
    }

    @Test
    public void testFindById() {
        when(supplyRepository.findById(1L)).thenReturn(java.util.Optional.of(supply));
        Supply foundSupply = supplyService.findById(1L);
        Assertions.assertEquals(supply, foundSupply);
    }

    @Test
    public void testFindByIdNotFound() {
        when(supplyRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        Assertions.assertThrows(SupplyNotFoundException.class, () -> supplyService.findById(1L));
    }

//    @Test
//    public void testCreateSupply() {
//        when(supplyRepository.findByName(createSupplyDto.getName())).thenReturn(java.util.Optional.empty());
//        supplyService.createSupply(createSupplyDto);
//        verify(supplyRepository).save(any(Supply.class));
//    }

    @Test
    public void testCreateSupplyAlreadyExists() {
        when(supplyRepository.findByName(createSupplyDto.getName())).thenReturn(java.util.Optional.of(supply));
        Assertions.assertThrows(SupplyAlreadyExistsException.class, () -> supplyService.createSupply(createSupplyDto));
    }

//    @Test
//    public void testUpdateSupply() {
//        when(supplyRepository.findById(1L)).thenReturn(java.util.Optional.of(supply));
//        supplyService.updateSupply(1L, updateSupplyDto);
//        verify(supplyRepository).save(any(Supply.class));
//    }

    @Test
    public void testUpdateSupplyNotFound() {
        when(supplyRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        Assertions.assertThrows(SupplyNotFoundException.class, () -> supplyService.updateSupply(1L, updateSupplyDto));
    }

    @Test
    public void testDeleteSupply() {
        when(supplyRepository.findById(1L)).thenReturn(java.util.Optional.of(supply));
        supplyService.removeSupply(1L);
        verify(supplyRepository).save(supply);
    }

    @Test
    public void testDeleteSupplyNotFound() {
        when(supplyRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        Assertions.assertThrows(SupplyNotFoundException.class, () -> supplyService.removeSupply(1L));
    }
}
