package com.dentalapp.backend.model.appointment.dtos;

import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.prescription.dtos.PrescriptionMapper;
import org.mapstruct.*;

import java.time.LocalTime;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {PrescriptionMapper.class})
public interface AppointmentMapper {

    @Mapping(target = "appointmentId", ignore = true)
    @Mapping(target = "appointmentStartTime", source = "appointmentStartTime", qualifiedByName = "stringToLocalTime")
    @Mapping(target = "appointmentEndTime", source = "appointmentEndTime", qualifiedByName = "stringToLocalTime")
    @Mapping(target = "isConfirmed", constant = "false")
    @Mapping(target = "isCancelled", constant = "false")
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "invoice", ignore = true)
    @Mapping(target = "prescriptions", ignore = true)
    @Mapping(target = "referrals", ignore = true)
    @Mapping(target = "files", ignore = true)
    @Mapping(target = "version", ignore = true)
    Appointment toAppointment(CreateAppointmentDto createAppointmentDto);

    @Mapping(target = "appointmentId", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "appointmentStartTime", source = "appointmentStartTime", qualifiedByName = "stringToLocalTime")
    @Mapping(target = "appointmentEndTime", source = "appointmentEndTime", qualifiedByName = "stringToLocalTime")
    @Mapping(target = "description", source = "appointmentDescription")
    @Mapping(target = "isConfirmed", ignore = true)
    @Mapping(target = "isCancelled", ignore = true)
    @Mapping(target = "invoice", ignore = true)
    @Mapping(target = "prescriptions", ignore = true)
    @Mapping(target = "referrals", ignore = true)
    @Mapping(target = "files", ignore = true)
    @Mapping(target = "version", ignore = true)
    Appointment toUpdateAppointment(@MappingTarget Appointment appointment, UpdateAppointmentDto updateAppointmentDto);

    @Named("stringToLocalTime")
    default LocalTime stringToLocalTime(String time) {
        return time != null ? LocalTime.parse(time) : null;
    }

    @Mapping(target = "appointmentStartTime", expression = "java(appointment.getAppointmentStartTime().toString())")
    @Mapping(target = "appointmentEndTime", expression = "java(appointment.getAppointmentEndTime().toString())")
    GetAppointmentDto toGetAppointmentDto(Appointment appointment);

    @Mapping(target = "doctorName", source = "doctor.firstName")
    @Mapping(target = "doctorLastName", source = "doctor.lastName")
    @Mapping(target = "doctorPhoneNumber", source = "doctor.phoneNumber")
    @Mapping(target = "appointmentStartTime", expression = "java(appointment.getAppointmentStartTime().toString())")
    @Mapping(target = "appointmentEndTime", expression = "java(appointment.getAppointmentEndTime().toString())")
    @Mapping(target = "confirmed", source = "isConfirmed")
    @Mapping(target = "cancelled", source = "isCancelled")
    @Mapping(target = "paid", source = "invoice.isPaid")
    GetAppointmentDtoV2 toGetAppointmentDtoV2(Appointment appointment);

    @Mapping(target = "patientInfo", expression = "java(appointment.getPatient().getFirstName() + \" \" + appointment.getPatient().getLastName())")
    @Mapping(target = "appointmentStartTime", expression = "java(appointment.getAppointmentStartTime().toString())")
    @Mapping(target = "appointmentEndTime", expression = "java(appointment.getAppointmentEndTime().toString())")
    GetAppointmentDtoV3 toGetAppointmentDtoV3(Appointment appointment);

    @Mapping(target = "patientName", source = "patient.firstName")
    @Mapping(target = "patientLastName", source = "patient.lastName")
    @Mapping(target = "patientPhoneNumber", source = "patient.phoneNumber")
    @Mapping(target = "appointmentStartTime", expression = "java(appointment.getAppointmentStartTime().toString())")
    @Mapping(target = "appointmentEndTime", expression = "java(appointment.getAppointmentEndTime().toString())")
    @Mapping(target = "confirmed", source = "isConfirmed")
    @Mapping(target = "cancelled", source = "isCancelled")
    @Mapping(target = "paid", source = "invoice.isPaid")
    GetAppointmentDtoV4 toGetAppointmentDtoV4(Appointment appointment);
}
