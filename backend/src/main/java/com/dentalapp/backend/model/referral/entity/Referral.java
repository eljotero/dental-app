package com.dentalapp.backend.model.referral.entity;

import com.dentalapp.backend.model.AuditClass;
import com.dentalapp.backend.model.appointment.entity.Appointment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "referrals")
@SQLRestriction("deleted_at IS NULL")
@Getter
@Setter
@SequenceGenerator(name="referral_id_seq", sequenceName = "referral_id_seq")
public class Referral extends AuditClass {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "referral_id_seq")
    @Column(name = "referral_id")
    private Long referralId;

    @Column(name="procedure_name", nullable = false)
    private String procedureName;

    @Column(name="procedure_description", nullable = false)
    private String procedureDescription;

    @Column(name="doctor_name")
    private String doctorName;

    @Column(name="clinic_name", nullable = false)
    private String clinicName;

    @Column(name="clinic_address")
    private String clinicAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;
}
