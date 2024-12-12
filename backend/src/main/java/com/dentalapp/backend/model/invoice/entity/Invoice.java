package com.dentalapp.backend.model.invoice.entity;

import com.dentalapp.backend.model.enums.PaymentStatus;
import com.dentalapp.backend.model.enums.PaymentType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invoice_id_seq")
    @SequenceGenerator(name = "invoice_id_seq", sequenceName = "invoice_id_seq", initialValue = 50, allocationSize = 1)
    @Column(name = "invoice_id")
    private Long invoiceId;

    @Column(name = "price")
    private Long price = 0L;

    @Column(name = "is_paid", nullable = false)
    private Boolean isPaid = false;

    @Column(name = "payment_method", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType = PaymentType.NONE;

    @Column(name = "payment_date")
    private LocalDate paymentDate = null;

    @Column(name = "payment_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.UNPAID;
}
