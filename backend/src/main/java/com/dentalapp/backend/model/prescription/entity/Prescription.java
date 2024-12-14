package com.dentalapp.backend.model.prescription.entity;

import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "prescriptions")
@Getter
@Setter
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prescription_id_seq")
    @SequenceGenerator(name = "prescription_id_seq", sequenceName = "prescription_id_seq", initialValue = 50, allocationSize = 1)
    @Column(name = "prescription_id")
    private Long prescriptionId;

    @Column(name = "medicine", nullable = false)
    private String medicine;

    @Column(name = "dosage", nullable = false)
    private String dosage;

    @ManyToOne
    @JoinColumn(name = "appointment_id", nullable = false)
    @JsonIgnore
    private Appointment appointment;
}
