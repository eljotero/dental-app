package com.dentalapp.backend.model.supplies.entity;

import com.dentalapp.backend.model.BaseEntityClass;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "supplies")
@SQLRestriction("deleted_at IS NULL")
@Getter
@Setter
@SequenceGenerator(name="supply_id_seq", sequenceName = "supply_id_seq")
@SQLDelete(sql = "UPDATE supplies SET deleted_at = CURRENT_TIMESTAMP WHERE supply_id = ? and version = ?")
public class Supply extends BaseEntityClass {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "supply_id_seq")
    @Column(name = "supply_id")
    private Long supplyId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "quantity", nullable = false)
    private Double quantity;

    @Column(name = "link")
    private String link;
}
