package com.dentalapp.backend.model.appointment.dtos;

import com.dentalapp.backend.model.file.dtos.GetFileDto;
import com.dentalapp.backend.model.prescription.dtos.GetPrescriptionDtoV2;
import com.dentalapp.backend.model.referral.dtos.GetReferralDto;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.util.List;

public record AppointmentDetailsResponse(Long appointmentId, String doctorName, String doctorLastName,
                                         String doctorPhoneNumber,
                                         @JsonInclude(JsonInclude.Include.NON_NULL) String patientName,
                                         @JsonInclude(JsonInclude.Include.NON_NULL) String patientLastName,
                                         @JsonInclude(JsonInclude.Include.NON_NULL) String patientPhoneNumber,
                                         LocalDate appointmentDate, String appointmentStartTime,
                                         String appointmentEndTime, String description, boolean confirmed,
                                         boolean cancelled, boolean paid, List<GetPrescriptionDtoV2> prescriptions,
                                         List<GetReferralDto> referrals, List<GetFileDto> files) {
}
