package com.dentalapp.backend.model.prescription.entity;

import com.dentalapp.backend.model.appointment.entity.Appointment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "prescriptions")
@Getter
@Setter
@SequenceGenerator(name = "prescription_id_seq", sequenceName = "prescription_id_seq")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prescription_id_seq")
    @Column(name = "prescription_id")
    private Long prescriptionId;

    @Column(name = "medicine", nullable = false)
    private String medicine;

    @Column(name = "dosage", nullable = false)
    private String dosage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;
}
