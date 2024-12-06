package com.dentalapp.backend.model.treatment.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "treatments")
@Data
public class Treatment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "treatment_id_seq")
    @SequenceGenerator(name = "treatment_id_seq", sequenceName = "treatment_id_seq", initialValue = 50, allocationSize = 1)
    @Column(name = "treatment_id")
    private Long treatmentId;

    @Column(name = "treatment_name")
    private String treatmentName;

    @Column(name = "treatment_description")
    private String treatmentDescription;

    @Column(name = "treatment_price")
    private Long treatmentPrice;

    @Column(name = "is_active")
    private Boolean isActive = true;
}
