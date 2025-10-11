package org.example.healthcare.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    private String full_name;
    @Column(unique = true, nullable = false)
    private String email;
    private String password_hash;
    private String phone_number;
    private String role;
    private String verfied;
    private Timestamp created_at;

}
