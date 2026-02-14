package com.dentalapp.backend.model.file.entity;

import com.dentalapp.backend.model.AuditClass;
import com.dentalapp.backend.model.appointment.entity.Appointment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "files")
@SQLRestriction("deleted_at IS NULL")
@Getter
@Setter
@SequenceGenerator(name="file_id_seq", sequenceName = "file_id_seq")
public class File extends AuditClass {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "file_id_seq")
    @Column(name = "file_id")
    private Long fileId;

    @Lob
    @Column(name = "file_data", nullable = false)
    private byte[] fileData;

    @Column(name = "file_name", nullable = false, unique = true)
    private String fileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;
}
