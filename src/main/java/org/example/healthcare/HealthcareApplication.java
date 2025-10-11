package org.example.healthcare;

import org.example.healthcare.model.Doctor;
import org.example.healthcare.repository.DoctorRepo;
import org.example.healthcare.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;

@SpringBootApplication
public class HealthcareApplication {

    public static void main(String[] args) {
        SpringApplication.run(HealthcareApplication.class, args);
    }

}
