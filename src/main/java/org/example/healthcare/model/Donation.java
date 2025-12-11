package org.example.healthcare.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Entity
@Table(name = "donations")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "donation_id")
    private Integer donationId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_id", referencedColumnName = "donorId")
    private Donor donor;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ngo_id", referencedColumnName = "ngo_id")
    private NGO ngo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "treatment_id", referencedColumnName = "treatment_id")
    private Treatment treatment;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", referencedColumnName = "equipment_id")
    private Equipment equipment;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supply_id", referencedColumnName = "supply_id")
    private Supply supply;
    
    @Column(name = "amount", precision = 12, scale = 2)
    private BigDecimal amount;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "donation_type", nullable = false)
    private DonationType donationType;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;
    
    @Column(name = "date_donated", nullable = false, updatable = false)
    private LocalDateTime dateDonated;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @PrePersist
    protected void onCreate() {
        if (dateDonated == null) {
            dateDonated = LocalDateTime.now();
        }
        if (paymentStatus == null) {
            paymentStatus = PaymentStatus.PENDING;
        }
        if (donationType == null) {
            donationType = DonationType.MONEY;
        }
    }
    
    public enum DonationType {
        MONEY, EQUIPMENT, SUPPLIES
    }
    
    public enum PaymentStatus {
        PENDING, COMPLETED, REFUNDED
    }
}

