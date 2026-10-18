package com.dentalapp.backend.model.availability.dtos;

import com.dentalapp.backend.model.commons.dtos.PaginationParams;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.time.LocalDate;

@Getter
@Setter
public class DoctorAvailabilityQueryParams extends PaginationParams<AvailabilitySortField> {

    @Serial
    private static final long serialVersionUID = -188988297956942499L;

    public DoctorAvailabilityQueryParams() {
        super(AvailabilitySortField.DATE, AvailabilitySortField.ID);
    }

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;
}
