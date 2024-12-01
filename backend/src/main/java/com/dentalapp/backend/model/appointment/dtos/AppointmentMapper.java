package com.dentalapp.backend.model.appointment.dtos;

import com.dentalapp.backend.model.appointment.entity.Appointment;

public class AppointmentMapper {
    public static Appointment toAppointment(CreateAppointmentDto createAppointmentDto) {
        Appointment appointment = new Appointment();
        appointment.setPatient(createAppointmentDto.getPatient());
        appointment.setDoctor(createAppointmentDto.getDoctor());
        appointment.setAppointmentDate(createAppointmentDto.getAppointmentDate());
        appointment.setAppointmentStartTime(createAppointmentDto.getAppointmentStartTime());
        appointment.setAppointmentEndTime(createAppointmentDto.getAppointmentEndTime());
        return appointment;
    }
}
