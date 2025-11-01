package org.example.healthcare.model;


import jakarta.persistence.*;
import lombok.*;

@Builder
@Entity
@Table(name = "doctor")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    private int doctorId;
    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "userId",referencedColumnName = "userId",foreignKey = @ForeignKey(name = "fk_doctor_user", foreignKeyDefinition = "FOREIGN KEY (userId) REFERENCES users(userId) ON DELETE CASCADE"))
    private User user;
    private String specialty;
    private String bio;


}
