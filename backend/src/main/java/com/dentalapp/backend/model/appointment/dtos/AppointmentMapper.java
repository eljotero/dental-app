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

    @Mapping(target = "patientInfo", expression = "java(includePatientInfo ? appointment.getPatient().getFirstName() + \" \" + appointment.getPatient().getLastName() : null)")
    @Mapping(target = "appointmentStartTime", expression = "java(appointment.getAppointmentStartTime().toString())")
    @Mapping(target = "appointmentEndTime", expression = "java(appointment.getAppointmentEndTime().toString())")
    MyAppointmentResponse toMyAppointmentResponse(Appointment appointment, boolean includePatientInfo);

    @Mapping(target = "doctorName", source = "appointment.doctor.firstName")
    @Mapping(target = "doctorLastName", source = "appointment.doctor.lastName")
    @Mapping(target = "doctorPhoneNumber", source = "appointment.doctor.phoneNumber")
    @Mapping(target = "patientName", expression = "java(includePatientInfo ? appointment.getPatient().getFirstName() : null)")
    @Mapping(target = "patientLastName", expression = "java(includePatientInfo ? appointment.getPatient().getLastName() : null)")
    @Mapping(target = "patientPhoneNumber", expression = "java(includePatientInfo ? appointment.getPatient().getPhoneNumber() : null)")
    @Mapping(target = "appointmentStartTime", expression = "java(appointment.getAppointmentStartTime().toString())")
    @Mapping(target = "appointmentEndTime", expression = "java(appointment.getAppointmentEndTime().toString())")
    @Mapping(target = "confirmed", source = "appointment.isConfirmed")
    @Mapping(target = "cancelled", source = "appointment.isCancelled")
    @Mapping(target = "paid", source = "appointment.invoice.isPaid")
    AppointmentDetailsResponse toAppointmentDetailsResponse(Appointment appointment, boolean includePatientInfo);
}
