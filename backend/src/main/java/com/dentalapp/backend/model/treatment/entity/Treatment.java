package com.dentalapp.backend.model.treatment.entity;

import com.dentalapp.backend.model.AuditClass;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "treatments")
@SQLRestriction("deleted_at IS NULL")
@Getter
@Setter
@SequenceGenerator(name="treatment_id_seq", sequenceName = "treatment_id_seq")
public class Treatment extends AuditClass {

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
