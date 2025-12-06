package org.example.healthcare.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "patient")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private int patientId;
    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "user_Id",referencedColumnName = "userId",foreignKey = @ForeignKey(name = "fk_patient_user", foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users(userId) ON DELETE CASCADE"))
    private User user;
    @Column(name = "date_of_birth")
    private Date dateOfBirth;
    private String gender;
    private String address;
    @Column(name = "medical_summary")
    private String medicalSummary;


}
