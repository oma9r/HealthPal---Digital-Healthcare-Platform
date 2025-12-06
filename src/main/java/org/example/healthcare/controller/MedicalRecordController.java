package org.example.healthcare.controller;

import org.example.healthcare.model.MedicalRecord;
import org.example.healthcare.model.User;
import org.example.healthcare.repository.UserRepo;
import org.example.healthcare.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordController {
    
    @Autowired
    private MedicalRecordService medicalRecordService;
    
    @Autowired
    private UserRepo userRepo;
    
    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN')")
    public ResponseEntity<List<MedicalRecord>> getPatientRecords(
            @PathVariable Integer patientId,
            @RequestParam(required = false) String type,
            Authentication auth) {
        String email = auth.getName();
        User user = userRepo.findByEmail(email);
        
        List<MedicalRecord> records;
        if (type != null) {
            try {
                MedicalRecord.RecordType recordType = MedicalRecord.RecordType.valueOf(type.toUpperCase());
                records = medicalRecordService.getRecordsByPatientIdAndType(patientId, recordType);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
        } else {
            records = medicalRecordService.getRecordsByPatientId(patientId);
        }
        
        return ResponseEntity.ok(records);
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<MedicalRecord> createRecord(
            @RequestBody MedicalRecord record,
            Authentication auth) {
        MedicalRecord created = medicalRecordService.createRecord(record);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN')")
    public ResponseEntity<MedicalRecord> getRecordById(
            @PathVariable Integer id,
            Authentication auth) {
        String email = auth.getName();
        
        return medicalRecordService.getRecordById(id)
            .map(record -> {
                if (medicalRecordService.canAccessRecord(email, record)) {
                    return ResponseEntity.ok(record);
                } else {
                    return ResponseEntity.<MedicalRecord>status(HttpStatus.FORBIDDEN).build();
                }
            })
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<MedicalRecord> updateRecord(
            @PathVariable Integer id,
            @RequestBody MedicalRecord record) {
        return ResponseEntity.ok(medicalRecordService.updateRecord(id, record));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<Void> deleteRecord(@PathVariable Integer id) {
        medicalRecordService.deleteRecord(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/type/{type}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<List<MedicalRecord>> getRecordsByType(@PathVariable String type) {
        try {
            MedicalRecord.RecordType recordType = MedicalRecord.RecordType.valueOf(type.toUpperCase());
            return ResponseEntity.ok(medicalRecordService.getRecordsByType(recordType));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

