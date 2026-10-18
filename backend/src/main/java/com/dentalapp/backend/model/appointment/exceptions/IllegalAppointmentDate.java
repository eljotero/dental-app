package com.dentalapp.backend.model.appointment.exceptions;

public class IllegalAppointmentDate extends RuntimeException {

    private IllegalAppointmentDate(String message) {
        super(message);
    }

    public static IllegalAppointmentDate slotAlreadyBooked() {
        return new IllegalAppointmentDate("Doctor already has an appointment at this time");
    }

    public static IllegalAppointmentDate doctorUnavailable() {
        return new IllegalAppointmentDate("Doctor is not available at this time");
    }
}
