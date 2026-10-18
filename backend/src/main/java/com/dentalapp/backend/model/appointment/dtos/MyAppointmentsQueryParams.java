package com.dentalapp.backend.model.appointment.dtos;

import com.dentalapp.backend.model.commons.dtos.PaginationParams;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.time.LocalDate;

@Getter
@Setter
public class MyAppointmentsQueryParams extends PaginationParams<AppointmentSortField> {

    @Serial
    private static final long serialVersionUID = 1L;

    public MyAppointmentsQueryParams() {
        super(AppointmentSortField.DATE, AppointmentSortField.ID);
    }

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;
}
