package com.dentalapp.backend.model.appointment.entity;

import com.dentalapp.backend.model.patient.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

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
    private Date appointmentDate;

    @Column(name = "is_confirmed")
    private Boolean isConfirmed;

    @Column(name = "description")
    private String description;
}
