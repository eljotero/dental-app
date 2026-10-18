package com.dentalapp.backend.model.invoice.repository;

import com.dentalapp.backend.model.invoice.dtos.InvoiceQueryParams;
import com.dentalapp.backend.model.invoice.entity.Invoice;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public final class InvoiceSpecification implements Specification<Invoice> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final InvoiceQueryParams params;

    public InvoiceSpecification(InvoiceQueryParams params) {
        this.params = params;
    }

    @Override
    public Predicate toPredicate(Root<Invoice> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (params.getStatus() != null) {
            predicates.add(cb.equal(root.get("paymentStatus"), params.getStatus()));
        }
        if (params.getMethod() != null) {
            predicates.add(cb.equal(root.get("paymentMethod"), params.getMethod()));
        }
        if (params.getDate() != null) {
            predicates.add(cb.equal(root.get("paymentDate"), params.getDate()));
        }

        return cb.and(predicates.toArray(Predicate[]::new));
    }
}
