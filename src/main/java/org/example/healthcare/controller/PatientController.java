package org.example.healthcare.controller;

import org.example.healthcare.model.Patient;
import org.example.healthcare.model.User;
import org.example.healthcare.repository.PatientRepo;
import org.example.healthcare.repository.UserRepo;
import org.example.healthcare.service.PatientService;
import org.example.healthcare.service.TreatmentService;
import org.example.healthcare.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {
    
    @Autowired
    private PatientService patientService;
    
    @Autowired
    private TreatmentService treatmentService;
    
    @Autowired
    private MedicalRecordService medicalRecordService;
    
    @Autowired
    private UserRepo userRepo;
    
    @Autowired
    private PatientRepo patientRepo;
    
    @GetMapping("/profile")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Patient> getMyProfile(Authentication auth) {
        String email = auth.getName();
        User user = userRepo.findByEmail(email);
        
        return patientService.getPatientByUserId(user.getUserId())
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/profile")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Patient> updateMyProfile(
            @RequestBody Patient patient,
            Authentication auth) {
        String email = auth.getName();
        User user = userRepo.findByEmail(email);
        
        return patientService.getPatientByUserId(user.getUserId())
            .map(p -> {
                Patient updated = patientService.updatePatient(p.getPatientId(), patient);
                return ResponseEntity.ok(updated);
            })
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<Patient> getPatientById(@PathVariable Integer id) {
        return patientService.getPatientById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/{id}/treatments")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN')")
    public ResponseEntity<List<org.example.healthcare.model.Treatment>> getPatientTreatments(
            @PathVariable Integer id,
            Authentication auth) {
        String email = auth.getName();
        User user = userRepo.findByEmail(email);
        
        // Verify access
        if (user.hasRole("PATIENT")) {
            Patient patient = patientRepo.findByUserUserId(user.getUserId());
            if (patient == null || patient.getPatientId() != id) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
        
        return ResponseEntity.ok(treatmentService.getTreatmentsByPatientId(id));
    }
    
    @GetMapping("/{id}/records")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN')")
    public ResponseEntity<List<org.example.healthcare.model.MedicalRecord>> getPatientRecords(
            @PathVariable Integer id,
            Authentication auth) {
        String email = auth.getName();
        User user = userRepo.findByEmail(email);
        
        // Verify access
        if (user.hasRole("PATIENT")) {
            Patient patient = patientRepo.findByUserUserId(user.getUserId());
            if (patient == null || patient.getPatientId() != id) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
        
        return ResponseEntity.ok(medicalRecordService.getRecordsByPatientId(id));
    }
}
