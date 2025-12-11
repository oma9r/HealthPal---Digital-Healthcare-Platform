package org.example.healthcare.model;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Entity
@Table(name = "ngo")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NGO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ngo_id")
    private Integer ngoId;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "userId", unique = true)
    private User user;
    
    @Column(name = "name", nullable = false, length = 150)
    private String name;
    
    @Column(name = "contact_info", columnDefinition = "TEXT")
    private String contactInfo;
    
    @Column(name = "verified", nullable = false)
    private Boolean verified;
    
    @PrePersist
    protected void onCreate() {
        if (verified == null) {
            verified = false;
        }
    }
}

