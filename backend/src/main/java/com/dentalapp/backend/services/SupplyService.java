package com.dentalapp.backend.services;

import com.dentalapp.backend.model.supplies.dtos.CreateSupplyDto;
import com.dentalapp.backend.model.supplies.dtos.SupplyMapper;
import com.dentalapp.backend.model.supplies.dtos.UpdateSupplyDto;
import com.dentalapp.backend.model.supplies.dtos.SupplyPageResponse;
import com.dentalapp.backend.model.supplies.dtos.SupplyQueryParams;
import com.dentalapp.backend.model.supplies.entity.Supply;
import com.dentalapp.backend.model.supplies.exceptions.SupplyAlreadyExistsException;
import com.dentalapp.backend.model.supplies.exceptions.SupplyNotFoundException;
import com.dentalapp.backend.model.supplies.repository.SupplyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS)
public class SupplyService {

    private final SupplyRepository supplyRepository;

    private final SupplyMapper supplyMapper;

    @Transactional(readOnly = true)
    public SupplyPageResponse<Supply> findAll(SupplyQueryParams params) {
        return SupplyPageResponse.from(supplyRepository.findAll(params.toPageable()));
    }

    public Supply findById(Long id) {
        return supplyRepository.findById(id).orElseThrow(() -> new SupplyNotFoundException(id));
    }

    @Transactional
    public void createSupply(CreateSupplyDto createSupplyDto) {
        if (supplyRepository.findByName(createSupplyDto.getName()).isPresent()) {
            throw new SupplyAlreadyExistsException(createSupplyDto.getName());
        }
        Supply supply = supplyMapper.toEntityCreate(createSupplyDto);
        supplyRepository.save(supply);
    }

    @Transactional
    public void updateSupply(Long id, UpdateSupplyDto updateSupplyDto) {
        Supply supply = supplyRepository.findById(id).orElseThrow(() -> new SupplyNotFoundException(id));
        Supply supplyDB = supplyMapper.toEntityUpdate(supply, updateSupplyDto);
        supplyRepository.save(supplyDB);
    }

    @Transactional
    public void removeSupply(Long id) {
        Supply supply = supplyRepository.findById(id).orElseThrow(() -> new SupplyNotFoundException(id));
        supplyRepository.delete(supply);
    }
}
