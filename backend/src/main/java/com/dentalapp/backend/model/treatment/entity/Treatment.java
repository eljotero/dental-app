package com.dentalapp.backend.model.treatment.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "treatments", indexes = {
        @Index(name = "idx_treatment_name", columnList = "treatment_name")
})
@Data
public class Treatment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "treatment_id_seq")
    @SequenceGenerator(name = "treatment_id_seq", sequenceName = "treatment_id_seq", initialValue = 50, allocationSize = 1)
    @Column(name = "treatment_id")
    private Long treatmentId;

    @Column(name = "treatment_name", nullable = false)
    private String treatmentName;

    @Column(name = "treatment_description", nullable = false)
    private String treatmentDescription;

    @Column(name = "treatment_price", nullable = false)
    private Long treatmentPrice;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
