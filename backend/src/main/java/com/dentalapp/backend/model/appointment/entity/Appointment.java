package com.dentalapp.backend.model.appointment.entity;

import com.dentalapp.backend.model.invoice.entity.Invoice;
import com.dentalapp.backend.model.prescription.entity.Prescription;
import com.dentalapp.backend.model.referral.entity.Referral;
import com.dentalapp.backend.model.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "appointments", indexes = {
        @Index(name = "idx_patient_id_is_cancelled", columnList = "patient_id, is_cancelled"),
        @Index(name = "idx_doctor_id_is_cancelled", columnList = "doctor_id, is_cancelled"),
        @Index(name = "idx_date_is_cancelled", columnList = "appointment_date, is_cancelled"),
        @Index(name="idx_doctor_id_date_is_cancelled", columnList = "doctor_id, appointment_date, is_cancelled"),
        @Index(name = "idx_is_confirmed_date", columnList = "is_confirmed, appointment_date"),
})
@Getter
@Setter
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "appointment_id_seq")
    @SequenceGenerator(name = "appointment_id_seq", sequenceName = "appointment_id_seq", initialValue = 50, allocationSize = 1)
    @Column(name = "appointment_id")
    private Long appointmentId;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private User doctor;

    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @Column(name = "appointment_start_time", nullable = false)
    private LocalTime appointmentStartTime;

    @Column(name = "appointment_end_time", nullable = false)
    private LocalTime appointmentEndTime;

    @Column(name = "is_confirmed", nullable = false)
    private Boolean isConfirmed = false;

    @Column(name = "is_cancelled", nullable = false)
    private Boolean isCancelled = false;

    @Column(name = "description")
    private String description;

    @OneToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    @OneToMany(mappedBy = "appointment")
    private List<Prescription> prescriptions;

    @OneToMany(mappedBy = "appointment")
    private List<Referral> referrals;

    @Version
    @JsonIgnore
    private Long version;
}
