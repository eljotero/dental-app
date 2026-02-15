package com.dentalapp.backend.unit.services;

import com.dentalapp.backend.model.supplies.dtos.CreateSupplyDto;
import com.dentalapp.backend.model.supplies.dtos.SupplyMapper;
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
class SupplyServiceTests {

    @Mock
    private SupplyRepository supplyRepository;

    @Mock
    private SupplyMapper supplyMapper;

    @InjectMocks
    private SupplyService supplyService;

    private Supply supply;

    private CreateSupplyDto createSupplyDto;

    private UpdateSupplyDto updateSupplyDto;

    @BeforeEach
    void setUp() {
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
    void testFindAll() {
        when(supplyRepository.findAll()).thenReturn(List.of(supply));
        List<Supply> supplies = supplyService.findAll();
        Assertions.assertEquals(List.of(supply), supplies);
    }

    @Test
    void testFindById() {
        when(supplyRepository.findById(1L)).thenReturn(java.util.Optional.of(supply));
        Supply foundSupply = supplyService.findById(1L);
        Assertions.assertEquals(supply, foundSupply);
    }

    @Test
    void testFindByIdNotFound() {
        when(supplyRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        Assertions.assertThrows(SupplyNotFoundException.class, () -> supplyService.findById(1L));
    }

    @Test
    void testCreateSupply() {
        Supply expectedSupply = new Supply();
        when(supplyRepository.findByName(createSupplyDto.getName())).thenReturn(java.util.Optional.empty());
        when(supplyMapper.toEntityCreate(createSupplyDto)).thenReturn(expectedSupply);
        supplyService.createSupply(createSupplyDto);
        verify(supplyMapper).toEntityCreate(createSupplyDto);
        verify(supplyRepository).save(expectedSupply);
    }


    @Test
    void testCreateSupplyAlreadyExists() {
        when(supplyRepository.findByName(createSupplyDto.getName())).thenReturn(java.util.Optional.of(supply));
        Assertions.assertThrows(SupplyAlreadyExistsException.class, () -> supplyService.createSupply(createSupplyDto));
    }

    @Test
    void testUpdateSupply() {
        Supply updatedSupply = new Supply();
        when(supplyRepository.findById(1L)).thenReturn(java.util.Optional.of(supply));
        when(supplyMapper.toEntityUpdate(supply, updateSupplyDto)).thenReturn(updatedSupply);
        when(supplyRepository.save(updatedSupply)).thenReturn(updatedSupply);
        supplyService.updateSupply(1L, updateSupplyDto);
        verify(supplyRepository).save(updatedSupply);
    }


    @Test
    void testUpdateSupplyNotFound() {
        when(supplyRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        Assertions.assertThrows(SupplyNotFoundException.class, () -> supplyService.updateSupply(1L, updateSupplyDto));
    }

    @Test
    void testDeleteSupply() {
        when(supplyRepository.findById(1L)).thenReturn(java.util.Optional.of(supply));
        supplyService.removeSupply(1L);
        verify(supplyRepository).delete(supply);
    }

    @Test
    void testDeleteSupplyNotFound() {
        when(supplyRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        Assertions.assertThrows(SupplyNotFoundException.class, () -> supplyService.removeSupply(1L));
    }
}
