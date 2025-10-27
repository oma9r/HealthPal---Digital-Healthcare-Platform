package org.example.healthcare.model;

import jakarta.persistence.*;

@Table(name = "patient")
public class Patient {
    @Id
    @Column(name = "patient_id")
    private int patientId;
    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "user_Id",referencedColumnName = "userId",foreignKey = @ForeignKey(name = "fk_patient_user", foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users(userId) ON DELETE CASCADE"))
    private User user;


}
