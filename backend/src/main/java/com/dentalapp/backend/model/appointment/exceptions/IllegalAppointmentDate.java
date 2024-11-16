package com.dentalapp.backend.model.appointment.exceptions;

public class IllegalAppointmentDate extends RuntimeException{
    public IllegalAppointmentDate(String message) {
        super(message);
    }
}
