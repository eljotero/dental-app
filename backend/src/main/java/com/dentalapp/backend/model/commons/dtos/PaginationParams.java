package com.dentalapp.backend.model.commons.dtos;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public abstract class PaginationParams<S extends Enum<S> & SortField> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Min(0)
    private int page = 0;

    @Min(1)
    @Max(100)
    private int size = 20;

    @NotNull
    private S sortBy;

    @NotNull
    private Sort.Direction direction = Sort.Direction.ASC;

    private final S tieBreaker;

    protected PaginationParams(S defaultSort, S tieBreaker) {
        this.sortBy = Objects.requireNonNull(defaultSort);
        this.tieBreaker = Objects.requireNonNull(tieBreaker);
    }

    @AssertTrue(message = "Page offset exceeds the supported range")
    public boolean isOffsetWithinRange() {
        return (long) page * size <= Integer.MAX_VALUE;
    }

    public Pageable toPageable() {
        Sort sort = Sort.by(direction, sortBy.getProperty());

        if (sortBy != tieBreaker) {
            sort = sort.and(Sort.by(tieBreaker.getProperty()));
        }

        return PageRequest.of(page, size, sort);
    }
}
