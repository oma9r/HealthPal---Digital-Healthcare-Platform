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
    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.EAGER)
    @JoinColumn(name = "userId",referencedColumnName = "userId",foreignKey = @ForeignKey(name = "fk_doctor_user", foreignKeyDefinition = "FOREIGN KEY (userId) REFERENCES users(userId) ON DELETE CASCADE"))
    private User user;
    private String speciality;
    private String bio;

    @Override
    public String toString() {
        return "User:"+user.toString()+"Speciality:"+speciality+"Bio:"+bio;
    }
}
