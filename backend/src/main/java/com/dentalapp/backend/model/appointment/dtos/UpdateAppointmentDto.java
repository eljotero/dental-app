package com.dentalapp.backend.model.appointment.dtos;

import com.dentalapp.backend.model.user.entity.User;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAppointmentDto {

    private Long doctorId;

    @Past(message = "Date of appointment must be in the past")
    private LocalDateTime appointmentDate;

    private String description;

    private User doctor;
}
