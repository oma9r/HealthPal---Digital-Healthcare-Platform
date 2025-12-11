package org.example.healthcare.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Builder
@Entity
@Table(name = "supplies")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Supply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supply_id")
    private Integer supplyId;
    
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @Column(name = "category", length = 50)
    private String category;
    
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    
    @Column(name = "expiry_date")
    private Date expiryDate;
    
    @Column(name = "location", length = 150)
    private String location;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ngo_id", referencedColumnName = "ngo_id")
    private NGO ngo;
    
    @PrePersist
    protected void onCreate() {
        if (quantity == null) {
            quantity = 0;
        }
    }
}

