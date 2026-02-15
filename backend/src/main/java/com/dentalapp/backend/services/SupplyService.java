package com.dentalapp.backend.services;

import com.dentalapp.backend.model.supplies.dtos.CreateSupplyDto;
import com.dentalapp.backend.model.supplies.dtos.SupplyMapper;
import com.dentalapp.backend.model.supplies.dtos.UpdateSupplyDto;
import com.dentalapp.backend.model.supplies.entity.Supply;
import com.dentalapp.backend.model.supplies.exceptions.SupplyAlreadyExistsException;
import com.dentalapp.backend.model.supplies.exceptions.SupplyNotFoundException;
import com.dentalapp.backend.model.supplies.repository.SupplyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class SupplyService {

    private final SupplyRepository supplyRepository;

    private final SupplyMapper supplyMapper;

    public List<Supply> findAll() {
        return supplyRepository.findAll();
    }

    public Supply findById(Long id) {
        return supplyRepository.findById(id).orElseThrow(() -> new SupplyNotFoundException("Supply with id " + id + " not found"));
    }

    @Transactional
    public void createSupply(CreateSupplyDto createSupplyDto) {
        if (supplyRepository.findByName(createSupplyDto.getName()).isPresent()) {
            throw new SupplyAlreadyExistsException("Supply with name " + createSupplyDto.getName() + " already exists");
        }
        Supply supply = supplyMapper.toEntityCreate(createSupplyDto);
        supplyRepository.save(supply);
    }

    @Transactional
    public void updateSupply(Long id, UpdateSupplyDto updateSupplyDto) {
        if (supplyRepository.findById(id).isEmpty()) {
            throw new SupplyNotFoundException("Supply with id " + id + " not found");
        }
        Supply supply = supplyRepository.findById(id).get();
        Supply supplyDB = supplyMapper.toEntityUpdate(supply, updateSupplyDto);
        supplyRepository.save(supplyDB);
    }

    @Transactional
    public void removeSupply(Long id) {
        Supply supply = supplyRepository.findById(id).orElseThrow(() -> new SupplyNotFoundException("Supply not found"));
        supplyRepository.delete(supply);
    }
}
