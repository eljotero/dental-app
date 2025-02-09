package com.dentalapp.backend.model.appointment.dtos;

import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.file.dtos.FileMapper;
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
        if (updateAppointmentDto.getAppointmentDescription() != null) {
            appointment.setDescription(updateAppointmentDto.getAppointmentDescription());
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
        getAppointmentDtoV2.setFiles(appointment.getFiles().stream().map(FileMapper::toGetFileDto).collect(Collectors.toCollection(ArrayList::new)));
        return getAppointmentDtoV2;
    }

    public static GetAppointmentDtoV3 toGetAppointmentDtoV3(Appointment appointment) {
        GetAppointmentDtoV3 getAppointmentDtoV3 = new GetAppointmentDtoV3();
        getAppointmentDtoV3.setAppointmentId(appointment.getAppointmentId());
        getAppointmentDtoV3.setPatientInfo(appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName());
        getAppointmentDtoV3.setAppointmentDate(appointment.getAppointmentDate());
        getAppointmentDtoV3.setAppointmentStartTime(appointment.getAppointmentStartTime().toString());
        getAppointmentDtoV3.setAppointmentEndTime(appointment.getAppointmentEndTime().toString());
        return getAppointmentDtoV3;
    }

    public static GetAppointmentDtoV4 toGetAppointmentDtoV4(Appointment appointment) {
        GetAppointmentDtoV4 getAppointmentDtoV4 = new GetAppointmentDtoV4();
        getAppointmentDtoV4.setPatientName(appointment.getPatient().getFirstName());
        getAppointmentDtoV4.setPatientLastName(appointment.getPatient().getLastName());
        getAppointmentDtoV4.setPatientPhoneNumber(appointment.getPatient().getPhoneNumber());
        getAppointmentDtoV4.setAppointmentDate(appointment.getAppointmentDate());
        getAppointmentDtoV4.setAppointmentStartTime(appointment.getAppointmentStartTime().toString());
        getAppointmentDtoV4.setAppointmentEndTime(appointment.getAppointmentEndTime().toString());
        getAppointmentDtoV4.setDescription(appointment.getDescription());
        getAppointmentDtoV4.setConfirmed(appointment.getIsConfirmed());
        getAppointmentDtoV4.setCancelled(appointment.getIsCancelled());
        getAppointmentDtoV4.setPaid(appointment.getInvoice().getIsPaid());
        if(appointment.getInvoice().getIsPaid().equals(Boolean.TRUE)) {
            getAppointmentDtoV4.setPaymentDate(appointment.getInvoice().getPaymentDate().toString());
            getAppointmentDtoV4.setPaymentMethod(appointment.getInvoice().getPaymentMethod().toString());
            getAppointmentDtoV4.setPaymentAmount(appointment.getInvoice().getPrice());
        }
        getAppointmentDtoV4.setPrescriptions(appointment.getPrescriptions().stream().map(PrescriptionMapper::toGetPrescriptionDtoV2).collect(Collectors.toCollection(ArrayList::new)));
        getAppointmentDtoV4.setReferrals(appointment.getReferrals().stream().map(ReferralMapper::toGetReferralDto).collect(Collectors.toCollection(ArrayList::new)));
        getAppointmentDtoV4.setFiles(appointment.getFiles().stream().map(FileMapper::toGetFileDto).collect(Collectors.toCollection(ArrayList::new)));
        return getAppointmentDtoV4;
    }
}
