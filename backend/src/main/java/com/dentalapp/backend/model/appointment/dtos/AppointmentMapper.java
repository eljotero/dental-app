package com.dentalapp.backend.model.appointment.dtos;

import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.prescription.dtos.PrescriptionMapper;
import com.dentalapp.backend.model.referral.dtos.ReferralMapper;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class AppointmentMapper {
    public static Appointment toAppointment(CreateAppointmentDto createAppointmentDto) {
        Appointment appointment = new Appointment();
        appointment.setPatient(createAppointmentDto.getPatient());
        appointment.setDoctor(createAppointmentDto.getDoctor());
        appointment.setAppointmentDate(createAppointmentDto.getAppointmentDate());
        appointment.setAppointmentStartTime(LocalTime.parse(createAppointmentDto.getAppointmentStartTime()));
        appointment.setAppointmentEndTime(LocalTime.parse(createAppointmentDto.getAppointmentEndTime()));
        return appointment;
    }

    public static Appointment toUpdateAppointment(Appointment appointment, UpdateAppointmentDto updateAppointmentDto) {
        if (updateAppointmentDto.getAppointmentDate() != null) {
            appointment.setAppointmentDate(updateAppointmentDto.getAppointmentDate());
        }
        if (updateAppointmentDto.getDescription() != null) {
            appointment.setDescription(updateAppointmentDto.getDescription());
        }
        if (updateAppointmentDto.getAppointmentStartTime() != null) {
            appointment.setAppointmentStartTime(LocalTime.parse(updateAppointmentDto.getAppointmentStartTime()));
        }
        if (updateAppointmentDto.getAppointmentEndTime() != null) {
            appointment.setAppointmentEndTime(LocalTime.parse(updateAppointmentDto.getAppointmentEndTime()));
        }
        return appointment;
    }

    public static GetAppointmentDto toGetAppointmentDto(Appointment appointment) {
        return new GetAppointmentDto(appointment.getAppointmentId(), appointment.getAppointmentDate(), appointment.getAppointmentStartTime().toString(), appointment.getAppointmentEndTime().toString());
    }

    public static GetAppointmentDtoV2 toGetAppointmentDtoV2(Appointment appointment) {
        GetAppointmentDtoV2 getAppointmentDtoV2 = new GetAppointmentDtoV2();
        getAppointmentDtoV2.setDoctorName(appointment.getDoctor().getFirstName());
        getAppointmentDtoV2.setDoctorLastName(appointment.getDoctor().getLastName());
        getAppointmentDtoV2.setDoctorPhoneNumber(appointment.getDoctor().getPhoneNumber());
        getAppointmentDtoV2.setAppointmentDate(appointment.getAppointmentDate());
        getAppointmentDtoV2.setAppointmentStartTime(appointment.getAppointmentStartTime().toString());
        getAppointmentDtoV2.setAppointmentEndTime(appointment.getAppointmentEndTime().toString());
        getAppointmentDtoV2.setDescription(appointment.getDescription());
        getAppointmentDtoV2.setConfirmed(appointment.getIsConfirmed());
        getAppointmentDtoV2.setCancelled(appointment.getIsCancelled());
        getAppointmentDtoV2.setPaid(appointment.getInvoice().getIsPaid());
        getAppointmentDtoV2.setPrescriptions(appointment.getPrescriptions().stream().map(PrescriptionMapper::toGetPrescriptionDtoV2).collect(Collectors.toCollection(ArrayList::new)));
        getAppointmentDtoV2.setReferrals(appointment.getReferrals().stream().map(ReferralMapper::toGetReferralDto).collect(Collectors.toCollection(ArrayList::new)));
        return getAppointmentDtoV2;
    }
}
