package org.example.healthcare.controller;

import org.example.healthcare.dto.DoctorDto;
import org.example.healthcare.model.Doctor;
import org.example.healthcare.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;



    @GetMapping("/all")
    public List<DoctorDto> getAllDoctors() {

       List <Doctor> docs =  doctorService.getAllDoctors();
       List<DoctorDto> dtos = new ArrayList<>();
       for (Doctor doc : docs) {
           DoctorDto dto = new DoctorDto();
           dto.setSpeciality(doc.getSpeciality());
           dto.setName(doc.getUser().getFullName());
           dto.setBio(doc.getBio());
           dto.setId(doc.getDoctorId());
           dtos.add(dto);
       }
       return dtos;

    }
    @GetMapping("/{id}")
    public DoctorDto getDoctorById( @PathVariable int id) {

        Doctor d = doctorService.getDoctorById(id);
        DoctorDto dto = new DoctorDto();
        dto.setId(d.getDoctorId());
        dto.setBio(d.getBio());
        dto.setSpeciality(d.getSpeciality());
        dto.setName(d.getUser().getFullName());
        return dto;
    }





}
