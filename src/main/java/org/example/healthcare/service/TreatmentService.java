package org.example.healthcare.service;

import org.example.healthcare.model.Patient;
import org.example.healthcare.model.Treatment;
import org.example.healthcare.repository.DonationRepo;
import org.example.healthcare.repository.PatientRepo;
import org.example.healthcare.repository.TreatmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class TreatmentService {
    
    @Autowired
    private TreatmentRepo treatmentRepo;
    
    @Autowired
    private PatientRepo patientRepo;
    
    @Autowired
    private DonationRepo donationRepo;
    
    public List<Treatment> getAllTreatments() {
        return treatmentRepo.findAll();
    }
    
    public List<Treatment> getActiveTreatments() {
        return treatmentRepo.findAllActiveTreatments();
    }
    
    public Optional<Treatment> getTreatmentById(Integer id) {
        return treatmentRepo.findById(id);
    }
    
    public List<Treatment> getTreatmentsByPatientId(Integer patientId) {
        return treatmentRepo.findByPatient_PatientId(patientId);
    }
    
    public List<Treatment> getTreatmentsByStatus(Treatment.TreatmentStatus status) {
        return treatmentRepo.findByStatus(status);
    }
    
    @Transactional
    public Treatment createTreatment(Treatment treatment) {
        if (treatment.getRaisedAmount() == null) {
            treatment.setRaisedAmount(BigDecimal.ZERO);
        }
        if (treatment.getStatus() == null) {
            treatment.setStatus(Treatment.TreatmentStatus.ACTIVE);
        }
        return treatmentRepo.save(treatment);
    }
    
    @Transactional
    public Treatment updateTreatment(Integer id, Treatment updatedTreatment) {
        return treatmentRepo.findById(id)
            .map(treatment -> {
                if (updatedTreatment.getDescription() != null) {
                    treatment.setDescription(updatedTreatment.getDescription());
                }
                if (updatedTreatment.getGoalAmount() != null) {
                    treatment.setGoalAmount(updatedTreatment.getGoalAmount());
                }
                if (updatedTreatment.getStartDate() != null) {
                    treatment.setStartDate(updatedTreatment.getStartDate());
                }
                if (updatedTreatment.getEndDate() != null) {
                    treatment.setEndDate(updatedTreatment.getEndDate());
                }
                if (updatedTreatment.getStatus() != null) {
                    treatment.setStatus(updatedTreatment.getStatus());
                }
                if (updatedTreatment.getTreatmentType() != null) {
                    treatment.setTreatmentType(updatedTreatment.getTreatmentType());
                }
                return treatmentRepo.save(treatment);
            })
            .orElseThrow(() -> new RuntimeException("Treatment not found with id: " + id));
    }
    
    @Transactional
    public void updateRaisedAmount(Integer treatmentId) {
        BigDecimal totalDonated = donationRepo.getTotalDonatedByTreatmentId(treatmentId);
        treatmentRepo.findById(treatmentId)
            .ifPresent(treatment -> {
                treatment.setRaisedAmount(totalDonated);
                
                // Auto-update status if goal is met
                if (treatment.getGoalAmount() != null && 
                    treatment.getGoalAmount().compareTo(BigDecimal.ZERO) > 0 &&
                    totalDonated.compareTo(treatment.getGoalAmount()) >= 0) {
                    treatment.setStatus(Treatment.TreatmentStatus.MET);
                }
                
                treatmentRepo.save(treatment);
            });
    }
    
    public BigDecimal calculateProgress(Integer treatmentId) {
        return treatmentRepo.findById(treatmentId)
            .map(treatment -> {
                if (treatment.getGoalAmount() == null || 
                    treatment.getGoalAmount().compareTo(BigDecimal.ZERO) == 0) {
                    return BigDecimal.ZERO;
                }
                BigDecimal totalDonated = donationRepo.getTotalDonatedByTreatmentId(treatmentId);
                return totalDonated
                    .divide(treatment.getGoalAmount(), 4, java.math.RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            })
            .orElse(BigDecimal.ZERO);
    }
    
    @Transactional
    public void deleteTreatment(Integer id) {
        treatmentRepo.deleteById(id);
    }
}

