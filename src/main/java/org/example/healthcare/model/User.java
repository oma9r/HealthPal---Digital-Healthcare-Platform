package org.example.healthcare.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Setter
@Getter
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    private String fullName;
    @Column(unique = true, nullable = false)
    private String email;
    private String passwordHash;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Boolean verified;
    private Timestamp createdAt;
    public int getUserId() {
        return userId;
    }


}
