package com.dentalapp.backend.model.appointment.dtos;

import com.dentalapp.backend.model.user.entity.User;
import com.dentalapp.backend.utils.constraints.UpdateTimesConstraint;
import jakarta.validation.constraints.Future;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@UpdateTimesConstraint(
        startTime = "appointmentStartTime",
        endTime = "appointmentEndTime",
        message = "Appointment start time must be before end time"
)
public class UpdateAppointmentDto {

    private Long doctorId;

    @Future(message = "Date of appointment must be in the future")
    private LocalDate appointmentDate;

    private String appointmentStartTime;

    private String appointmentEndTime;

    private String appointmentDescription;

    private User doctor;
}