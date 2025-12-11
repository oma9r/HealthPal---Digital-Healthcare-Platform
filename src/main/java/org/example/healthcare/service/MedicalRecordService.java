package org.example.healthcare.service;

import org.example.healthcare.model.MedicalRecord;
import org.example.healthcare.model.Patient;
import org.example.healthcare.repository.MedicalRecordRepo;
import org.example.healthcare.repository.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MedicalRecordService {
    
    @Autowired
    private MedicalRecordRepo medicalRecordRepo;
    
    @Autowired
    private PatientRepo patientRepo;
    
    public List<MedicalRecord> getAllRecords() {
        return medicalRecordRepo.findAll();
    }
    
    public Optional<MedicalRecord> getRecordById(Integer id) {
        return medicalRecordRepo.findById(id);
    }
    
    public List<MedicalRecord> getRecordsByPatientId(Integer patientId) {
        return medicalRecordRepo.findByPatient_PatientId(patientId);
    }
    
    public List<MedicalRecord> getRecordsByType(MedicalRecord.RecordType recordType) {
        return medicalRecordRepo.findByRecordType(recordType);
    }
    
    public List<MedicalRecord> getRecordsByPatientIdAndType(Integer patientId, MedicalRecord.RecordType recordType) {
        return medicalRecordRepo.findByPatient_PatientIdAndRecordType(patientId, recordType);
    }
    
    @Transactional
    public MedicalRecord createRecord(MedicalRecord record) {
        if (record.getRecordType() == null) {
            record.setRecordType(MedicalRecord.RecordType.NOTES);
        }
        return medicalRecordRepo.save(record);
    }
    
    @Transactional
    public MedicalRecord updateRecord(Integer id, MedicalRecord updatedRecord) {
        return medicalRecordRepo.findById(id)
            .map(record -> {
                if (updatedRecord.getDescription() != null) {
                    record.setDescription(updatedRecord.getDescription());
                }
                if (updatedRecord.getDocumentUrl() != null) {
                    record.setDocumentUrl(updatedRecord.getDocumentUrl());
                }
                if (updatedRecord.getDateOfRecord() != null) {
                    record.setDateOfRecord(updatedRecord.getDateOfRecord());
                }
                if (updatedRecord.getRecordType() != null) {
                    record.setRecordType(updatedRecord.getRecordType());
                }
                return medicalRecordRepo.save(record);
            })
            .orElseThrow(() -> new RuntimeException("Medical record not found with id: " + id));
    }
    
    @Transactional
    public void deleteRecord(Integer id) {
        medicalRecordRepo.deleteById(id);
    }
    
    public boolean canAccessRecord(String userEmail, MedicalRecord record) {
        // Implementation would check if user (doctor/patient/admin) can access the record
        // This is a placeholder - should be enhanced with proper authorization logic
        return true;
    }
}

