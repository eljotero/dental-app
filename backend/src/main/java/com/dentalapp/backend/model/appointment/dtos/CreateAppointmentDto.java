package com.dentalapp.backend.model.appointment.dtos;

import com.dentalapp.backend.model.user.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAppointmentDto {

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    @NotNull(message = "Appointment date is required")
    private LocalDate appointmentDate;

    @NotNull(message = "Appointment start time is required")
    private String appointmentStartTime;

    @NotNull(message = "Appointment duration is required")
    private String appointmentEndTime;

//    @AssertTrue
//    public boolean isEndTimeValid() {
//        if(appointmentStartTime == null || appointmentEndTime == null) {
//            return false;
//        }
//        return appointmentEndTime.isAfter(appointmentStartTime);
//    }

    private User patient;

    private User doctor;
}
