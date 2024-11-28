package com.dentalapp.backend.model.appointment.dtos;

import com.dentalapp.backend.model.user.entity.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAppointmentDto {

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    @NotNull(message = "Appointment date is required")
    @Past(message = "Date of appointment must be in the past")
    private LocalDateTime appointmentDate;

    @NotNull(message = "Appointment duration is required")
    private LocalTime appointmentDuration;

    private User patient;

    private User doctor;
}
