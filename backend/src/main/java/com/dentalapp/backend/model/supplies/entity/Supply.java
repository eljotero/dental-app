package com.dentalapp.backend.model.supplies.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "supplies", indexes = {
        @Index(name = "idx_name", columnList = "name")
})
@Getter
@Setter
public class Supply {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "supply_id_seq")
    @SequenceGenerator(name = "supply_id_seq", sequenceName = "supply_id_seq", initialValue = 50, allocationSize = 1)
    @Column(name = "supply_id")
    private Long supplyId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "quantity", nullable = false)
    private Double quantity;

    @Column(name = "link")
    private String link;
}
