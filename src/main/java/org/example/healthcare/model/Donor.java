package org.example.healthcare.model;


import jakarta.persistence.*;
import lombok.*;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "donor")
public class Donor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "donorId")
    private int donorId;
    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "userId",referencedColumnName = "userId",foreignKey = @ForeignKey(name = "fk_donor_user", foreignKeyDefinition = "FOREIGN KEY (userId) REFERENCES users(userId) ON DELETE CASCADE"))
    private User user;
    private String organization;


}
