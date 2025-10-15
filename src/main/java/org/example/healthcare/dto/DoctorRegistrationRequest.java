package org.example.healthcare.dto;

import lombok.Data;

@Data
public class DoctorRegistrationRequest {
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private String specialty;
    private String bio;
}

