package org.example.healthcare.service;

import org.example.healthcare.model.Doctor;
import org.example.healthcare.repository.DoctorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService{
    @Autowired
    DoctorRepo doctorRepo;
    public List<Doctor> getAllDoctors(){
        return doctorRepo.findAll();
    }
    public Doctor getDoctorById(int id){
        return doctorRepo.findById(id).get();
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
