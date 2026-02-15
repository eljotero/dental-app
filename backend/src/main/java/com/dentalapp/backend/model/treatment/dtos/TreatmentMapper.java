package com.dentalapp.backend.model.treatment.dtos;

import com.dentalapp.backend.model.treatment.entity.Treatment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
public interface TreatmentMapper {

    Treatment toEntityCreate(CreateTreatmentDto createTreatmentDto);

    Treatment toEntityUpdate(@MappingTarget Treatment treatment, UpdateTreatmentDto updateTreatmentDto);
}
