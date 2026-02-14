package com.dentalapp.backend.model.appointment.dtos;

import com.dentalapp.backend.model.user.entity.User;
import com.dentalapp.backend.utils.constraints.CreateTimesConstraint;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@CreateTimesConstraint(
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