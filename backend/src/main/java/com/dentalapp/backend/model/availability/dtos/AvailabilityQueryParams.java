package com.dentalapp.backend.model.availability.dtos;

import com.dentalapp.backend.model.commons.dtos.PaginationParams;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.time.LocalDate;

@Getter
@Setter
public class AvailabilityQueryParams extends PaginationParams<AvailabilitySortField> {

    @Serial
    private static final long serialVersionUID = 5720494980303312326L;

    public AvailabilityQueryParams() {
        super(AvailabilitySortField.DATE, AvailabilitySortField.ID);
    }

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    @Positive
    private Long id;
}
