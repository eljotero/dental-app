package com.dentalapp.backend.model.supplies.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "supplies")
public class Supply {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "supply_id_seq")
    @SequenceGenerator(name = "supply_id_seq", sequenceName = "supply_id_seq", initialValue = 50, allocationSize = 1)
    @Column(name = "supply_id")
    private Long supplyId;

    @Column(name = "name")
    private String name;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private Double price;
}
