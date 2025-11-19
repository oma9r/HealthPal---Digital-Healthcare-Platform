package org.example.healthcare.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Builder
@Entity
@Table(name = "consultation")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class Consultation {
    @Id
    @Column(name ="consultation_id")
    private int id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="patient_id", referencedColumnName = "patient_id")
    private Patient patient;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id",referencedColumnName = "doctor_id")
    private Doctor doctor;
    @Column(name = "scheduled_at", nullable = false)
    private LocalDateTime scheduledTime;

    @Column(length = 20)
    private String status; // requested, confirmed, completed, cancelled

    @Column(length = 20)
    private String mode; // video, audio, chat
    @Column(name = "low_bandwidth")
    private Boolean lowBandwidth;

    @Column(columnDefinition = "TEXT")
    private String notes;
    @Column(name = "created_at")
    private LocalDateTime createdAt;


}
