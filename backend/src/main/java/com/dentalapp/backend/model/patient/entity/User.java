package com.dentalapp.backend.model.patient.entity;

import com.dentalapp.backend.model.enums.UserType;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "patient_id_seq")
    @SequenceGenerator(name = "patient_id_seq", sequenceName = "patient_id_seq", initialValue = 50, allocationSize = 1)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String email;

    @Column(name = "phone_number")
    private String phone;

    private Boolean sex;

    @Column(name = "personal_id_number")
    private String personalId;

    private String country;

    private String city;

    private String address;

    @Enumerated(EnumType.STRING)
    private UserType userType;
}
