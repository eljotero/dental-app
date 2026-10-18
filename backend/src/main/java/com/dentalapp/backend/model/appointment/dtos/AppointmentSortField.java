package com.dentalapp.backend.model.appointment.dtos;

import com.dentalapp.backend.model.commons.dtos.SortField;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AppointmentSortField implements SortField {
    DATE("appointmentDate"),
    START_TIME("appointmentStartTime"),
    ID("appointmentId");

    private final String property;
}
