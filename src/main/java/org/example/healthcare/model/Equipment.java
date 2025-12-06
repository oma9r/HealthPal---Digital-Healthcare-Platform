package org.example.healthcare.model;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Entity
@Table(name = "equipment")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "equipment_id")
    private Integer equipmentId;
    
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "condition", length = 50)
    private String condition;
    
    @Column(name = "location", length = 150)
    private String location;
    
    @Column(name = "available", nullable = false)
    private Boolean available;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ngo_id", referencedColumnName = "ngo_id")
    private NGO ngo;
    
    @PrePersist
    protected void onCreate() {
        if (available == null) {
            available = true;
        }
    }
}

