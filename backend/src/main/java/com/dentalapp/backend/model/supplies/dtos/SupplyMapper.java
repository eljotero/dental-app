package com.dentalapp.backend.model.supplies.dtos;

import com.dentalapp.backend.model.supplies.entity.Supply;

public class SupplyMapper {

    public static Supply toEntityCreate(CreateSupplyDto createSupplyDto) {
        Supply supply = new Supply();
        supply.setName(createSupplyDto.getName());
        supply.setQuantity(createSupplyDto.getQuantity());
        supply.setLink(createSupplyDto.getLink());
        return supply;
    }

    public static Supply toEntityUpdate(Supply supply, UpdateSupplyDto updateSupplyDto) {
        if (updateSupplyDto.getName() != null) {
            supply.setName(updateSupplyDto.getName());
        }
        if(updateSupplyDto.getQuantity() != null) {
            supply.setQuantity(updateSupplyDto.getQuantity());
        }
        if(updateSupplyDto.getLink() != null) {
            supply.setLink(updateSupplyDto.getLink());
        }
        return supply;
    }
}
