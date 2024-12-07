package com.dentalapp.backend.model.appointment.entity;

import com.dentalapp.backend.model.invoice.entity.Invoice;
import com.dentalapp.backend.model.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "appointment_id_seq")
    @SequenceGenerator(name = "appointment_id_seq", sequenceName = "appointment_id_seq", initialValue = 50, allocationSize = 1)
    @Column(name = "appointment_id")
    private Long appointmentId;

    @OneToOne
    @JoinColumn(name = "patient_id")
    private User patient;

    @OneToOne
    @JoinColumn(name = "doctor_id")
    private User doctor;

    @Column(name = "appointment_date")
    private LocalDate appointmentDate;

    @Column(name = "appointment_start_time")
    private LocalTime appointmentStartTime;

    @Column(name = "appointment_end_time")
    private LocalTime appointmentEndTime;

    @Column(name = "is_confirmed")
    private Boolean isConfirmed = false;

    @Column(name = "is_cancelled")
    private Boolean isCancelled = false;

    @Column(name = "description")
    private String description;

    @OneToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    @Version
    private Long version;
}
