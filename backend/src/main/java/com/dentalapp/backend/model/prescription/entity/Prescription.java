package com.dentalapp.backend.model.prescription.entity;

import com.dentalapp.backend.model.BaseEntityClass;
import com.dentalapp.backend.model.appointment.entity.Appointment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "prescriptions")
@SQLRestriction("deleted_at IS NULL")
@Getter
@Setter
@SequenceGenerator(name = "prescription_id_seq", sequenceName = "prescription_id_seq")
@SQLDelete(sql = "UPDATE prescriptions SET deleted_at = CURRENT_TIMESTAMP WHERE prescription_id = ? and version = ?")
public class Prescription extends BaseEntityClass {

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
