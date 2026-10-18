package com.dentalapp.backend.model.appointment.repository;

import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.enums.UserType;
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

public final class MyAppointmentsSpecification implements Specification<Appointment> {

    @Serial
    private static final long serialVersionUID = 7072795639997867111L;

    private final Long userId;
    private final UserType userType;
    private final LocalDate date;

    public MyAppointmentsSpecification(Long userId, UserType userType, LocalDate date) {

        this.userId = Objects.requireNonNull(userId);
        this.userType = Objects.requireNonNull(userType);
        this.date = date;
    }

    @Override
    public Predicate toPredicate(Root<Appointment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        String participantField = switch (userType) {
            case PATIENT -> "patient";
            case DOCTOR -> "doctor";
            default -> null;
        };

        if (participantField == null) {
            return cb.disjunction();
        }

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(root.get(participantField).get("userId"), userId));

        predicates.add(cb.isFalse(root.get("isCancelled")));

        if (date != null) {
            predicates.add(cb.equal(root.get("appointmentDate"), date));
        }

        return cb.and(predicates.toArray(Predicate[]::new));
    }
}