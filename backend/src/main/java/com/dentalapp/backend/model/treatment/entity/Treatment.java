package com.dentalapp.backend.model.treatment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "treatments")
@Getter
@Setter
@SequenceGenerator(name="treatment_id_seq", sequenceName = "treatment_id_seq")
public class Treatment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "treatment_id_seq")
    @Column(name = "treatment_id")
    private Long treatmentId;

    @Column(name = "treatment_name", nullable = false)
    private String treatmentName;

    @Column(name = "treatment_description", nullable = false)
    private String treatmentDescription;

    @Column(name = "treatment_price", nullable = false)
    private Long treatmentPrice;
}
