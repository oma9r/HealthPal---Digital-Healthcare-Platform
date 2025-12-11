package org.example.healthcare.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Date;

@Builder
@Entity
@Table(name = "treatment")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Treatment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "treatment_id")
    private Integer treatmentId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", referencedColumnName = "patient_id")
    private Patient patient;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "treatment_type", nullable = false)
    private TreatmentType treatmentType;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "goal_amount", precision = 12, scale = 2)
    private BigDecimal goalAmount;
    
    @Column(name = "raised_amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal raisedAmount;
    
    @Column(name = "start_date")
    private Date startDate;
    
    @Column(name = "end_date")
    private Date endDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TreatmentStatus status;
    
    @PrePersist
    protected void onCreate() {
        if (raisedAmount == null) {
            raisedAmount = BigDecimal.ZERO;
        }
        if (status == null) {
            status = TreatmentStatus.ACTIVE;
        }
        if (treatmentType == null) {
            treatmentType = TreatmentType.SURGERY;
        }
    }
    
    public enum TreatmentType {
        SURGERY, CANCER, DIALYSIS, REHAB
    }
    
    public enum TreatmentStatus {
        ACTIVE, MET, CLOSED
    }
}

