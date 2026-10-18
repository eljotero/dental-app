package com.dentalapp.backend.model.appointment.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;

public record MyAppointmentResponse(
        Long appointmentId,
        LocalDate appointmentDate,
        String appointmentStartTime,
        String appointmentEndTime,
        @JsonInclude(JsonInclude.Include.NON_NULL) String patientInfo
) {
}
