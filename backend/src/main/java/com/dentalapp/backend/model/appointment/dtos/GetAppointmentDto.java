package com.dentalapp.backend.model.appointment.dtos;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAppointmentDto {
    private Long appointmentId;
    private LocalDate appointmentDate;
    private String appointmentStartTime;
    private String appointmentEndTime;
}
