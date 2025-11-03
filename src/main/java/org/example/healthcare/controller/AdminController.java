package org.example.healthcare.controller;

import org.example.healthcare.model.Doctor;
import org.example.healthcare.model.User;
import org.example.healthcare.service.DoctorService;
import org.example.healthcare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    UserService userService;
    @Autowired
    DoctorService doctorService;
    @GetMapping
    public String hello(){
        return "admin";
    }
    @GetMapping("/doctors/all")
    public List<Doctor> getAllDoctors() {
        return doctorService.getAllDoctors();
    }
    @PostMapping("/doctors/id/")
    public Doctor getDoctorById(@RequestParam("id") int docId) {
        return doctorService.getDoctorById(docId);
    }
    @PostMapping("/doctors/add/")
    public Doctor addDoctor(@RequestBody Doctor doctor,@RequestParam String email) {
        User user = userService.getByEmail(email);
        doctor.setUser(user);
        return doctorService.saveDoctor(doctor);
    }
    @GetMapping("/users/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    @PostMapping("/users/id/")
    public User getUserById(@RequestParam("id") int userId) {
        return userService.getUserById(userId);
    }
    @PostMapping("/users/add/")
    public String addUser(@RequestBody User user) {
        try {
            return userService.addUser(user).toString();
        }catch (Exception e) {
            return e.toString();
        }

    }
}
