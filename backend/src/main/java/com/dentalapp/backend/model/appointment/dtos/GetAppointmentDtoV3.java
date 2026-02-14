package com.dentalapp.backend.model.appointment.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetAppointmentDtoV3 {

    private Long appointmentId;

    private String patientInfo;

    private LocalDate appointmentDate;

    private String appointmentStartTime;

    private String appointmentEndTime;
}
