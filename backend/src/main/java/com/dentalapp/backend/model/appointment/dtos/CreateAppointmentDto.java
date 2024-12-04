package com.dentalapp.backend.model.appointment.dtos;

import com.dentalapp.backend.model.user.entity.User;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    private LocalDate appointmentDate;

    @NotNull(message = "Appointment start time is required")
    private LocalTime appointmentStartTime;

    @NotNull(message = "Appointment duration is required")
    private LocalTime appointmentEndTime;

    @AssertTrue
    public boolean isEndTimeValid() {
        if(appointmentStartTime == null || appointmentEndTime == null) {
            return false;
        }
        return appointmentEndTime.isAfter(appointmentStartTime);
    }

    private User patient;

    private User doctor;
}
