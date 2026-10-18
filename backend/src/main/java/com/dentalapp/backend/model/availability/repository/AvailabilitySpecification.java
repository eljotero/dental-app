package com.dentalapp.backend.model.availability.repository;

import com.dentalapp.backend.model.availability.dtos.AvailabilityQueryParams;
import com.dentalapp.backend.model.availability.dtos.DoctorAvailabilityQueryParams;
import com.dentalapp.backend.model.availability.entity.Availability;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serial;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class AvailabilitySpecification implements Specification<Availability> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final LocalDate date;
    private final Long doctorId;

    public AvailabilitySpecification(AvailabilityQueryParams params) {
        this.date = params.getDate();
        this.doctorId = params.getId();
    }

    public AvailabilitySpecification(Long doctorId, DoctorAvailabilityQueryParams params) {
        this.date = params.getDate();
        this.doctorId = Objects.requireNonNull(doctorId);
    }

    @Override
    public Predicate toPredicate(Root<Availability> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (date != null) {
            predicates.add(cb.equal(root.get("availabilityDate"), date));
        }
        if (doctorId != null) {
            predicates.add(cb.equal(root.get("doctor").get("userId"), doctorId));
        }

        return cb.and(predicates.toArray(Predicate[]::new));
    }
}
