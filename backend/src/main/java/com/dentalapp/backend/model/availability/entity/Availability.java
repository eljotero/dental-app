package com.dentalapp.backend.model.availability.entity;

import com.dentalapp.backend.model.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "availabilities")
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "availability_id_seq")
    @SequenceGenerator(name = "availability_id_seq", sequenceName = "availability_id_seq", initialValue = 50, allocationSize = 1)
    @Column(name = "availability_id")
    private Long availabilityId;

    @OneToOne
    @JoinColumn(name = "doctor_id")
    private User doctor;

    @Column(name = "availability_date")
    private LocalDate availabilityDate;

    @Column(name = "availability_start_time")
    private LocalTime availabilityStartTime;

    @Column(name = "availability_end_time")
    private LocalTime availabilityEndTime;

    @Column(name = "is_confirmed")
    private Boolean isConfirmed = false;

    @Column(name = "brake_time_start")
    private LocalTime brakeTimeStart;

    @Column(name = "brake_time_end")
    private LocalTime brakeTimeEnd;

    @Version
    private Long version;
}
