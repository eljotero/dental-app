package com.dentalapp.backend.model.appointment.dtos;

import com.dentalapp.backend.model.file.dtos.GetFileDto;
import com.dentalapp.backend.model.prescription.dtos.GetPrescriptionDtoV2;
import com.dentalapp.backend.model.referral.dtos.GetReferralDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetAppointmentDtoV2 {

    private String doctorName;

    private String doctorLastName;

    private String doctorPhoneNumber;

    private LocalDate appointmentDate;

    private String appointmentStartTime;

    private String appointmentEndTime;

    private String description;

    private boolean isConfirmed;

    private boolean isCancelled;

    private boolean isPaid;

    private ArrayList<GetPrescriptionDtoV2> prescriptions;

    private ArrayList<GetReferralDto> referrals;

    private ArrayList<GetFileDto> files;
}
