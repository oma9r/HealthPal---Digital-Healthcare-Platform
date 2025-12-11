package org.example.healthcare.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.time.LocalDateTime;

@Builder
@Entity
@Table(name = "medical_record")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MedicalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Integer recordId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", referencedColumnName = "patient_id")
    private Patient patient;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "record_type", nullable = false)
    private RecordType recordType;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "document_url", columnDefinition = "TEXT")
    private String documentUrl;
    
    @Column(name = "date_of_record")
    private Date dateOfRecord;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (recordType == null) {
            recordType = RecordType.NOTES;
        }
    }
    
    public enum RecordType {
        DIAGNOSIS, LAB, SURGERY, NOTES
    }
}

