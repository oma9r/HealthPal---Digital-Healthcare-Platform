package org.example.healthcare.controller;

import org.example.healthcare.model.Doctor;
import org.example.healthcare.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;



    @GetMapping("/all")
    public List<Doctor> getAllDoctors() {

       return doctorService.getAllDoctors();

    }
    @GetMapping("/id/")
    public Doctor getDoctorById(@RequestParam int id) {
        return doctorService.getDoctorById(id);
    }





}
