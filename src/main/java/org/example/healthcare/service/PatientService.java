package org.example.healthcare.service;

import org.example.healthcare.model.Patient;
import org.example.healthcare.model.User;
import org.example.healthcare.repository.PatientRepo;
import org.example.healthcare.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {
    
    @Autowired
    private PatientRepo patientRepo;
    
    @Autowired
    private UserRepo userRepo;
    
    public List<Patient> getAllPatients() {
        return patientRepo.findAll();
    }
    
    public Optional<Patient> getPatientById(Integer id) {
        return patientRepo.findById(id);
    }
    
    public Optional<Patient> getPatientByUserId(Integer userId) {
        return Optional.ofNullable(patientRepo.findByUserUserId(userId));
    }
    
    @Transactional
    public Patient createPatient(Patient patient) {
        return patientRepo.save(patient);
    }
    
    @Transactional
    public Patient updatePatient(Integer id, Patient updatedPatient) {
        return patientRepo.findById(id)
            .map(patient -> {
                if (updatedPatient.getDateOfBirth() != null) {
                    patient.setDateOfBirth(updatedPatient.getDateOfBirth());
                }
                if (updatedPatient.getGender() != null) {
                    patient.setGender(updatedPatient.getGender());
                }
                if (updatedPatient.getAddress() != null) {
                    patient.setAddress(updatedPatient.getAddress());
                }
                if (updatedPatient.getMedicalSummary() != null) {
                    patient.setMedicalSummary(updatedPatient.getMedicalSummary());
                }
                return patientRepo.save(patient);
            })
            .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
    }
    
    @Transactional
    public void deletePatient(Integer id) {
        patientRepo.deleteById(id);
    }
}

