package com.dentalapp.backend.model.referral.dtos;

import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.referral.entity.Referral;

public class ReferralMapper {
    public static Referral toEntity(CreateReferralDto createReferralDto, Appointment appointment) {
        Referral referral = new Referral();
        referral.setProcedureName(createReferralDto.getProcedureName());
        referral.setProcedureDescription(createReferralDto.getProcedureDescription());
        if (createReferralDto.getDoctorName() != null) {
            referral.setDoctorName(createReferralDto.getDoctorName());
        }
        referral.setClinicName(createReferralDto.getClinicName());
        if (createReferralDto.getClinicAddress() != null) {
            referral.setClinicAddress(createReferralDto.getClinicAddress());
        }
        referral.setAppointment(appointment);
        return referral;
    }

    public static Referral toUpdateEntity(Referral referral, UpdateReferralDto updateReferralDto) {
        if (updateReferralDto.getProcedureName() != null) {
            referral.setProcedureName(updateReferralDto.getProcedureName());
        }
        if (updateReferralDto.getProcedureDescription() != null) {
            referral.setProcedureDescription(updateReferralDto.getProcedureDescription());
        }
        if (updateReferralDto.getDoctorName() != null) {
            referral.setDoctorName(updateReferralDto.getDoctorName());
        }
        if (updateReferralDto.getClinicName() != null) {
            referral.setClinicName(updateReferralDto.getClinicName());
        }
        if (updateReferralDto.getClinicAddress() != null) {
            referral.setClinicAddress(updateReferralDto.getClinicAddress());
        }
        return referral;
    }
}
