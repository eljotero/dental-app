package com.dentalapp.backend.model.referral.dtos;

import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.referral.entity.Referral;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ReferralMapper {

    @Mapping(target = "referralId", ignore = true)
    @Mapping(target = "appointment", source = "appointment")
    @Mapping(target = "procedureName", source = "dto.procedureName")
    @Mapping(target = "procedureDescription", source = "dto.procedureDescription")
    @Mapping(target = "doctorName", source = "dto.doctorName")
    @Mapping(target = "clinicName", source = "dto.clinicName")
    @Mapping(target = "clinicAddress", source = "dto.clinicAddress")
    Referral toEntity(CreateReferralDto dto, Appointment appointment);

    @Mapping(target = "referralId", ignore = true)
    @Mapping(target = "appointment", ignore = true)
    Referral toUpdateEntity(@MappingTarget Referral referral, UpdateReferralDto updateReferralDto);

    GetReferralDto toGetReferralDto(Referral referral);
}