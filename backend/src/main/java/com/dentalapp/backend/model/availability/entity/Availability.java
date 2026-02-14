package com.dentalapp.backend.model.availability.entity;

import com.dentalapp.backend.model.AuditClass;
import com.dentalapp.backend.model.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "availabilities")
@SQLRestriction("deleted_at IS NULL")
@Getter
@Setter
@SequenceGenerator(name="availability_id_seq", sequenceName = "availability_id_seq")
public class Availability extends AuditClass {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "availability_id_seq")
    @Column(name = "availability_id")
    private Long availabilityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private User doctor;

    @Column(name = "availability_date", nullable = false)
    private LocalDate availabilityDate;

    @Column(name = "availability_start_time", nullable = false)
    private LocalTime availabilityStartTime;

    @Column(name = "availability_end_time", nullable = false)
    private LocalTime availabilityEndTime;

    @Column(name = "is_confirmed", nullable = false)
    private Boolean isConfirmed = false;

    @Column(name = "brake_time_start")
    private LocalTime brakeTimeStart;

    @Column(name = "brake_time_end")
    private LocalTime brakeTimeEnd;

    @Version
    private Long version;
}
