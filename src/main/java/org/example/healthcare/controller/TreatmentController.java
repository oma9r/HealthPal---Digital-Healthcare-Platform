package org.example.healthcare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.healthcare.model.Patient;
import org.example.healthcare.model.Treatment;
import org.example.healthcare.model.User;
import org.example.healthcare.repository.PatientRepo;
import org.example.healthcare.repository.UserRepo;
import org.example.healthcare.service.TreatmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/treatments")
@Tag(name = "Treatment", description = "Medical treatment and sponsorship management APIs")
@SecurityRequirement(name = "bearerAuth")
public class TreatmentController {
    
    @Autowired
    private TreatmentService treatmentService;
    
    @Autowired
    private UserRepo userRepo;
    
    @Autowired
    private PatientRepo patientRepo;
    
    @GetMapping
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN', 'DONOR')")
    @Operation(summary = "Get all treatments", description = "Retrieve all treatments, optionally filtered by status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved treatments"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<Treatment>> getAllTreatments(
            @RequestParam(required = false) String status) {
        if (status != null) {
            try {
                Treatment.TreatmentStatus treatmentStatus = Treatment.TreatmentStatus.valueOf(status.toUpperCase());
                return ResponseEntity.ok(treatmentService.getTreatmentsByStatus(treatmentStatus));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.ok(treatmentService.getAllTreatments());
    }
    
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN', 'DONOR')")
    public ResponseEntity<List<Treatment>> getActiveTreatments() {
        return ResponseEntity.ok(treatmentService.getActiveTreatments());
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN', 'DONOR')")
    public ResponseEntity<Treatment> getTreatmentById(@PathVariable Integer id) {
        return treatmentService.getTreatmentById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    
    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN')")
    public ResponseEntity<List<Treatment>> getPatientTreatments(@PathVariable Integer patientId) {
        return ResponseEntity.ok(treatmentService.getTreatmentsByPatientId(patientId));
    }
    
    @PostMapping
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Treatment> createTreatment(
            @RequestBody Treatment treatment,
            Authentication auth) {
        String email = auth.getName();
        User user = userRepo.findByEmail(email);
        Patient patient = patientRepo.findByUserUserId(user.getUserId());
        
        if (patient == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        treatment.setPatient(patient);
        Treatment created = treatmentService.createTreatment(treatment);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('PATIENT', 'ADMIN')")
    public ResponseEntity<Treatment> updateTreatment(
            @PathVariable Integer id,
            @RequestBody Treatment treatment,
            Authentication auth) {
        String email = auth.getName();
        User user = userRepo.findByEmail(email);
        
        // Verify ownership if not admin
        if (!user.hasRole("ADMIN")) {
            return treatmentService.getTreatmentById(id)
                .<ResponseEntity<Treatment>>map(t -> {
                    if (t.getPatient().getUser().getUserId() != user.getUserId()) {
                        return ResponseEntity.<Treatment>status(HttpStatus.FORBIDDEN).build();
                    }
                    return ResponseEntity.ok(treatmentService.updateTreatment(id, treatment));
                })
                .orElse(ResponseEntity.<Treatment>notFound().build());
        }
        
        return ResponseEntity.ok(treatmentService.updateTreatment(id, treatment));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('PATIENT', 'ADMIN')")
    public ResponseEntity<Void> deleteTreatment(@PathVariable Integer id, Authentication auth) {
        String email = auth.getName();
        User user = userRepo.findByEmail(email);
        
        if (!user.hasRole("ADMIN")) {
            return treatmentService.getTreatmentById(id)
                .<ResponseEntity<Void>>map(t -> {
                    if (t.getPatient().getUser().getUserId() != user.getUserId()) {
                        return ResponseEntity.<Void>status(HttpStatus.FORBIDDEN).build();
                    }
                    treatmentService.deleteTreatment(id);
                    return ResponseEntity.<Void>noContent().build();
                })
                .orElse(ResponseEntity.<Void>notFound().build());
        }
        
        treatmentService.deleteTreatment(id);
        return ResponseEntity.noContent().build();
    }
}

