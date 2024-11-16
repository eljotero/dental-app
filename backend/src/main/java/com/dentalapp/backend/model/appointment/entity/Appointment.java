package com.dentalapp.backend.model.appointment.entity;

import com.dentalapp.backend.model.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
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
    private LocalDateTime appointmentDate;

    @Column(name = "appointment_duration")
    private LocalTime appointmentDuration;

    @Column(name = "is_confirmed")
    private Boolean isConfirmed;

    @Column(name = "is_cancelled")
    private Boolean isCancelled = false;

    @Column(name = "description")
    private String description;

    @Version
    private Long version;
}
