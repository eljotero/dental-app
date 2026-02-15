package com.dentalapp.backend.model.prescription.dtos;

import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.prescription.entity.Prescription;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PrescriptionMapper {

    @Mapping(source = "createPrescriptionDto.medicineName", target = "medicine")
    @Mapping(source = "createPrescriptionDto.dosage", target = "dosage")
    Prescription toEntity(CreatePrescriptionDto createPrescriptionDto, Appointment appointment);

    @Mapping(source = "updatePrescriptionDto.medicineName", target = "medicine")
    @Mapping(source = "updatePrescriptionDto.dosage", target = "dosage")
    Prescription toUpdateEntity(@MappingTarget Prescription prescription, UpdatePrescriptionDto updatePrescriptionDto);

    @Mapping(source = "medicine", target = "medicineName")
    @Mapping(source = "appointment.appointmentId", target = "appointmentId")
    GetPrescriptionDto toGetPrescriptionDto(Prescription prescription);

    @Mapping(source = "medicine", target = "medicineName")
    GetPrescriptionDtoV2 toGetPrescriptionDtoV2(Prescription prescription);
}
