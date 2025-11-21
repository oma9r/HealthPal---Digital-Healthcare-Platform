package org.example.healthcare.model;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.NotBlank;


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
    @NotBlank(message = "name cant be blank")
    @Column(nullable = false)
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


    public boolean hasRole(String role) {
        return this.role.name().equals(role);
    }

    public String toString(){
        return "ID:"+ userId+"Name: " + fullName + ", Email: " + email + ", Phone: " + phoneNumber + ", Role: " + role;


    }


}
