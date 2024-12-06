package com.dentalapp.backend.model.appointment.dtos;

import com.dentalapp.backend.model.user.entity.User;
import com.dentalapp.backend.utils.constraints.CreateAppointmentTimesConstraint;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@CreateAppointmentTimesConstraint(
        startTime = "appointmentStartTime",
        endTime = "appointmentEndTime",
        message = "Appointment start time must be before end time"
)
public class CreateAppointmentDto {

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    @NotNull(message = "Appointment date is required")
    @Future(message = "Appointment date must be in the future")
    private LocalDate appointmentDate;

    @NotNull(message = "Appointment start time is required")
    private String appointmentStartTime;

    @NotNull(message = "Appointment end time is required")
    private String appointmentEndTime;

    private User patient;

    private User doctor;
}