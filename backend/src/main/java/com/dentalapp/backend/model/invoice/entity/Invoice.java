package com.dentalapp.backend.model.invoice.entity;

import com.dentalapp.backend.model.AuditClass;
import com.dentalapp.backend.model.enums.PaymentStatus;
import com.dentalapp.backend.model.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;

@Entity
@Table(name = "invoices")
@SQLRestriction("deleted_at IS NULL")
@Getter
@Setter
@SequenceGenerator(name="invoice_id_seq", sequenceName = "invoice_id_seq")
public class Invoice extends AuditClass {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invoice_id_seq")
    @Column(name = "invoice_id")
    private Long invoiceId;

    @Column(name = "price")
    private Long price = 0L;

    @Column(name = "is_paid", nullable = false)
    private Boolean isPaid = false;

    @Column(name = "payment_method", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod = PaymentMethod.NONE;

    @Column(name = "payment_date")
    private LocalDate paymentDate = null;

    @Column(name = "payment_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.UNPAID;
}
