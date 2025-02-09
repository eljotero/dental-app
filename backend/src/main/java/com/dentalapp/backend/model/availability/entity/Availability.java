package com.dentalapp.backend.model.availability.entity;

import com.dentalapp.backend.model.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "availabilities", indexes = {
        @Index(name = "idx_doctor_id_date_start_time_end_time", columnList = "doctor_id, availability_date, availability_start_time, availability_end_time"),
        @Index(name = "idx_doctor_id_date_start_time_end_time_brake_time", columnList = "doctor_id, availability_date, availability_start_time, availability_end_time, brake_time_start, brake_time_end"),
        @Index(name = "idx_doctor_id", columnList = "doctor_id")
})
@Getter
@Setter
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "availability_id_seq")
    @SequenceGenerator(name = "availability_id_seq", sequenceName = "availability_id_seq", initialValue = 50, allocationSize = 1)
    @Column(name = "availability_id")
    private Long availabilityId;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private User doctor;

    @Column(name = "availability_date", nullable = false)
    private LocalDate availabilityDate;

    @Column(name = "availability_start_time", nullable = false)
    private LocalTime availabilityStartTime;

    @Column(name = "availability_end_time", nullable = false)
    private LocalTime availabilityEndTime;

    @Column(name = "brake_time_start")
    private LocalTime brakeTimeStart;

    @Column(name = "brake_time_end")
    private LocalTime brakeTimeEnd;

    @Version
    private Long version;
}
