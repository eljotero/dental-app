package com.dentalapp.backend.model.supplies.dtos;

import com.dentalapp.backend.model.supplies.entity.Supply;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SupplyMapper {

    Supply toEntityCreate(CreateSupplyDto createSupplyDto);

    Supply toEntityUpdate(@MappingTarget Supply supply, UpdateSupplyDto updateSupplyDto);
}
