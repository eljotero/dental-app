package com.dentalapp.backend.model.prescription.dtos;

import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.prescription.entity.Prescription;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PrescriptionMapper {

    @Mapping(source = "createPrescriptionDto.medicineName", target = "medicine")
    @Mapping(source = "createPrescriptionDto.dosage", target = "dosage")
    @Mapping(source = "appointment", target = "appointment")
    Prescription toEntity(CreatePrescriptionDto createPrescriptionDto, Appointment appointment);

    @Mapping(source = "updatePrescriptionDto.medicineName", target = "medicine")
    @Mapping(source = "updatePrescriptionDto.dosage", target = "dosage")
    Prescription toUpdateEntity(@MappingTarget Prescription prescription, UpdatePrescriptionDto updatePrescriptionDto);

    @Mapping(source = "prescriptionId", target = "prescriptionId")
    @Mapping(source = "medicine", target = "medicineName")
    @Mapping(source = "dosage", target = "dosage")
    @Mapping(source = "appointment.appointmentId", target = "appointmentId")
    GetPrescriptionDto toGetPrescriptionDto(Prescription prescription);

    @Mapping(source = "prescriptionId", target = "prescriptionId")
    @Mapping(source = "medicine", target = "medicineName")
    @Mapping(source = "dosage", target = "dosage")
    GetPrescriptionDtoV2 toGetPrescriptionDtoV2(Prescription prescription);
}
