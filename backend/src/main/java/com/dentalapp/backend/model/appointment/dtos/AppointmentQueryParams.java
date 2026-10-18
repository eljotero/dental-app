package com.dentalapp.backend.model.appointment.dtos;

import com.dentalapp.backend.model.commons.dtos.PaginationParams;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.time.LocalDate;

@Getter
@Setter
public class AppointmentQueryParams extends PaginationParams<AppointmentSortField> {

    @Serial
    private static final long serialVersionUID = -4510810796387209762L;

    public AppointmentQueryParams() {
        super(AppointmentSortField.DATE, AppointmentSortField.ID);
    }

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateTo;

    @Positive
    private Long doctorId;

    @Positive
    private Long patientId;

    private Boolean isConfirmed;

    private Boolean isCancelled;

}
