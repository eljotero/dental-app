package com.dentalapp.backend.model.appointment.dtos;

import com.dentalapp.backend.model.appointment.entity.Appointment;

import java.time.LocalTime;

public class AppointmentMapper {
    public static Appointment toAppointment(CreateAppointmentDto createAppointmentDto) {
        Appointment appointment = new Appointment();
        appointment.setPatient(createAppointmentDto.getPatient());
        appointment.setDoctor(createAppointmentDto.getDoctor());
        appointment.setAppointmentDate(createAppointmentDto.getAppointmentDate());
        appointment.setAppointmentStartTime(LocalTime.parse(createAppointmentDto.getAppointmentStartTime()));
        appointment.setAppointmentEndTime(LocalTime.parse(createAppointmentDto.getAppointmentEndTime()));
        return appointment;
    }

    public static Appointment toUpdateAppointment(Appointment appointment, UpdateAppointmentDto updateAppointmentDto) {
        if (updateAppointmentDto.getAppointmentDate() != null) {
            appointment.setAppointmentDate(updateAppointmentDto.getAppointmentDate());
        }
        if (updateAppointmentDto.getDescription() != null) {
            appointment.setDescription(updateAppointmentDto.getDescription());
        }
        if (updateAppointmentDto.getAppointmentStartTime() != null) {
            appointment.setAppointmentStartTime(LocalTime.parse(updateAppointmentDto.getAppointmentStartTime()));
        }
        if (updateAppointmentDto.getAppointmentEndTime() != null) {
            appointment.setAppointmentEndTime(LocalTime.parse(updateAppointmentDto.getAppointmentEndTime()));
        }
        return appointment;
    }
}
