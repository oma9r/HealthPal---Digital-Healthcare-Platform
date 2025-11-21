package org.example.healthcare.controller;

import lombok.RequiredArgsConstructor;
import org.example.healthcare.dto.ConsultationRequest;
import org.example.healthcare.model.Consultation;
import org.example.healthcare.model.Doctor;
import org.example.healthcare.model.Patient;
import org.example.healthcare.model.User;
import org.example.healthcare.repository.DoctorRepo;
import org.example.healthcare.repository.PatientRepo;
import org.example.healthcare.repository.UserRepo;
import org.example.healthcare.service.ConsultationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/consultations")
@RequiredArgsConstructor
public class ConsultationController {
    @Autowired
    private final ConsultationService consultationService;
    @Autowired
    private DoctorRepo doctorRepo;
    @Autowired
    private PatientRepo patientRepo;
    @Autowired
    private UserRepo userRepo;

    // Get all consultations for logged-in user
    @GetMapping
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN')")
    public List<Consultation> getMyConsultations(Authentication auth) {
        String email = auth.getName();
        System.out.println(email);

        return consultationService.getConsultationsForUser(email);
    }

    // Get one consultation by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN')")
    public ResponseEntity<Consultation> getConsultationById(
            @PathVariable int id,
            Authentication auth) {

        Consultation consultation = consultationService.getConsultationById(id);
        String userEmail = auth.getName();

        // Check ownership
        if (!consultationService.canAccessConsultation(userEmail, consultation)) {
            throw new AccessDeniedException("You are not allowed to access this consultation.");
        }

        return ResponseEntity.ok(consultation);
    }

    // Create a new consultation
    @PostMapping
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Consultation> createConsultation(
            @RequestBody ConsultationRequest request,
            Authentication auth) {

        String email = auth.getName();

        Doctor doctor = doctorRepo.findByDoctorId(request.getDoctorId());
        User user = userRepo.findByEmail(email);
        Patient patient = patientRepo.findByUserUserId(user.getUserId());
        Consultation consultation = new Consultation();
        consultation.setDoctor(doctor);
        consultation.setPatient(patient);
        consultation.setScheduledTime(request.getScheduledAt());
        consultation.setMode(request.getMode());
        consultation.setLowBandwidth(request.isLowBandwidth());
        consultation.setNotes(request.getNotes());
        consultation.setStatus("SCHEDULED");
        consultation.setCreatedAt(LocalDateTime.now());

        Consultation created = consultationService.createConsultation(consultation);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

}
