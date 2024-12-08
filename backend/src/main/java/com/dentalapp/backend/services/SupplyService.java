package com.dentalapp.backend.services;

import com.dentalapp.backend.model.supplies.dtos.CreateSupplyDto;
import com.dentalapp.backend.model.supplies.dtos.SupplyMapper;
import com.dentalapp.backend.model.supplies.dtos.UpdateSupplyDto;
import com.dentalapp.backend.model.supplies.entity.Supply;
import com.dentalapp.backend.model.supplies.exceptions.SupplyAlreadyExistsException;
import com.dentalapp.backend.model.supplies.exceptions.SupplyNotFoundException;
import com.dentalapp.backend.model.supplies.repository.SupplyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SupplyService {

    private final SupplyRepository supplyRepository;

    public SupplyService(SupplyRepository supplyRepository) {
        this.supplyRepository = supplyRepository;
    }

    public List<Supply> findAll() {
        return supplyRepository.findAll();
    }

    public Supply findById(Long id) {
        return supplyRepository.findById(id).orElseThrow(() -> new SupplyNotFoundException("Supply with id " + id + " not found"));
    }

    public Supply findByName(String name) {
        return supplyRepository.findByName(name).orElseThrow(() -> new SupplyNotFoundException("Supply with name " + name + " not found"));
    }

    @Transactional
    public void createSupply(CreateSupplyDto createSupplyDto) {
        if (supplyRepository.findByName(createSupplyDto.getName()).isPresent()) {
            throw new SupplyAlreadyExistsException("Supply with name " + createSupplyDto.getName() + " already exists");
        }
        Supply supply = SupplyMapper.toEntityCreate(createSupplyDto);
        supplyRepository.save(supply);
    }

    @Transactional
    public void updateSupply(Long id, UpdateSupplyDto updateSupplyDto) {
        if (supplyRepository.findById(id).isEmpty()) {
            throw new SupplyNotFoundException("Supply with id " + id + " not found");
        }
        Supply supply = supplyRepository.findById(id).get();
        Supply supplyDB = SupplyMapper.toEntityUpdate(supply, updateSupplyDto);
        supplyRepository.save(supplyDB);
    }

    @Transactional
    public void removeSupply(Long id) {
        if (supplyRepository.findById(id).isEmpty()) {
            throw new SupplyNotFoundException("Supply with id " + id + " not found");
        }
        supplyRepository.deleteById(id);
    }
}
