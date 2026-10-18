package com.dentalapp.backend.model.appointment.repository;

import com.dentalapp.backend.model.appointment.dtos.AppointmentQueryParams;
import com.dentalapp.backend.model.appointment.entity.Appointment;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public final class AppointmentSpecification implements Specification<Appointment> {

    private final AppointmentQueryParams params;

    @Nullable
    @Override
    public Predicate toPredicate(Root<Appointment> root, @Nullable CriteriaQuery<?> query, CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<>();

        if (params.getDateFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("appointmentDate"), params.getDateFrom()));
        }

        if (params.getDateTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("appointmentDate"), params.getDateTo()));
        }

        if (params.getDoctorId() != null) {
            predicates.add(cb.equal(root.get("doctor").get("userId"), params.getDoctorId()));
        }

        if (params.getPatientId() != null) {
            predicates.add(cb.equal(root.get("patient").get("userId"), params.getPatientId()));
        }

        if (params.getIsConfirmed() != null) {
            predicates.add(cb.equal(root.get("isConfirmed"), params.getIsConfirmed()));
        }

        if (params.getIsCancelled() != null) {
            predicates.add(cb.equal(root.get("isCancelled"), params.getIsCancelled()));
        }

        return cb.and(predicates.toArray(Predicate[]::new));
    }
}
