package com.dentalapp.backend.model.prescription.entity;

import com.dentalapp.backend.model.appointment.entity.Appointment;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "prescriptions")
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prescription_id_seq")
    @SequenceGenerator(name = "prescription_id_seq", sequenceName = "prescription_id_seq", initialValue = 50, allocationSize = 1)
    @Column(name = "prescription_id")
    private Long prescriptionId;

    @Column(nullable = false, name = "medicine")
    private String medicine;

    @Column(nullable = false, name = "dosage")
    private String dosage;

    @ManyToOne
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;
}
