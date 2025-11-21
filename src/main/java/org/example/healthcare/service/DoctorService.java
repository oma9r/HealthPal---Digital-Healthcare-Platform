package org.example.healthcare.service;

import org.example.healthcare.dto.DoctorRegistrationRequest;
import org.example.healthcare.model.Doctor;
import org.example.healthcare.model.Role;
import org.example.healthcare.model.User;
import org.example.healthcare.repository.DoctorRepo;
import org.example.healthcare.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;


@Service
public class DoctorService{
    @Autowired
    DoctorRepo doctorRepo;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepo userRepo;
    public List<Doctor> getAllDoctors(){
        return doctorRepo.findAll();
    }
    public Doctor registerDoctor(DoctorRegistrationRequest request) {
        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .passwordHash(request.getPassword())
                .role(Role.DOCTOR)
                .verified(false)
                .build();

        userRepo.save(user);

        Doctor doctor = Doctor.builder()
                .speciality(request.getSpecialty())
                .bio(request.getBio())
                .user(user)
                .build();

        return doctorRepo.save(doctor);
    }

    public Doctor getDoctorById(int id){
        return doctorRepo.findByDoctorId(id);
    }
    public Doctor saveDoctor(Doctor doctor){
        return doctorRepo.save(doctor);
    }
    public void deleteDoctor(int id){
        doctorRepo.deleteById(id);
    }
    public Doctor updateDoctor(Doctor doctor){
        return doctorRepo.save(doctor);
    }
    public Doctor getDoctorByUserId(int userId){
        return doctorRepo.findByUser_UserId(userId);
    }

}
