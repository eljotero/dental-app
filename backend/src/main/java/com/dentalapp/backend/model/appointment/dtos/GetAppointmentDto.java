package com.dentalapp.backend.model.appointment.dtos;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetAppointmentDto {

    private Long appointmentId;

    private LocalDate appointmentDate;

    private String appointmentStartTime;

    private String appointmentEndTime;
}
