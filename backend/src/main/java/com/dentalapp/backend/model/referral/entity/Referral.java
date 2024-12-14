package com.dentalapp.backend.model.referral.entity;

import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "referrals")
@Getter
@Setter
public class Referral {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "referral_id_seq")
    @SequenceGenerator(name = "referral_id_seq", sequenceName = "referral_id_seq", initialValue = 50, allocationSize = 1)
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

    @ManyToOne
    @JoinColumn(name = "appointment_id", nullable = false)
    @JsonIgnore
    private Appointment appointment;
}
