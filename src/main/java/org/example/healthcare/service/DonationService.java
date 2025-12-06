package org.example.healthcare.service;

import org.example.healthcare.model.Donation;
import org.example.healthcare.model.Donor;
import org.example.healthcare.model.NGO;
import org.example.healthcare.model.Treatment;
import org.example.healthcare.repository.DonationRepo;
import org.example.healthcare.repository.DonorRepo;
import org.example.healthcare.repository.NGORepo;
import org.example.healthcare.repository.TreatmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class DonationService {
    
    @Autowired
    private DonationRepo donationRepo;
    
    @Autowired
    private DonorRepo donorRepo;
    
    @Autowired
    private TreatmentRepo treatmentRepo;
    
    @Autowired
    private NGORepo ngoRepo;
    
    @Autowired
    private TreatmentService treatmentService;
    
    public List<Donation> getAllDonations() {
        return donationRepo.findAll();
    }
    
    public Optional<Donation> getDonationById(Integer id) {
        return donationRepo.findById(id);
    }
    
    public List<Donation> getDonationsByDonorId(Integer donorId) {
        return donorRepo.findById(donorId)
            .map(donationRepo::findByDonor)
            .orElse(List.of());
    }
    
    public List<Donation> getDonationsByTreatmentId(Integer treatmentId) {
        return donationRepo.findAllByTreatmentId(treatmentId);
    }
    
    public List<Donation> getDonationsByType(Donation.DonationType donationType) {
        return donationRepo.findByDonationType(donationType);
    }
    
    public List<Donation> getDonationsByStatus(Donation.PaymentStatus paymentStatus) {
        return donationRepo.findByPaymentStatus(paymentStatus);
    }
    
    public BigDecimal getTotalDonatedByTreatmentId(Integer treatmentId) {
        return donationRepo.getTotalDonatedByTreatmentId(treatmentId);
    }
    
    @Transactional
    public Donation createDonation(Donation donation) {
        // Set default values if not provided
        if (donation.getPaymentStatus() == null) {
            donation.setPaymentStatus(Donation.PaymentStatus.PENDING);
        }
        if (donation.getDonationType() == null) {
            donation.setDonationType(Donation.DonationType.MONEY);
        }
        
        Donation savedDonation = donationRepo.save(donation);
        
        // Update treatment raised amount if this is a money donation for a treatment
        if (donation.getTreatment() != null && 
            donation.getDonationType() == Donation.DonationType.MONEY &&
            donation.getPaymentStatus() == Donation.PaymentStatus.COMPLETED) {
            treatmentService.updateRaisedAmount(donation.getTreatment().getTreatmentId());
        }
        
        return savedDonation;
    }
    
    @Transactional
    public Donation updatePaymentStatus(Integer donationId, Donation.PaymentStatus newStatus) {
        return donationRepo.findById(donationId)
            .map(donation -> {
                Donation.PaymentStatus oldStatus = donation.getPaymentStatus();
                donation.setPaymentStatus(newStatus);
                Donation updatedDonation = donationRepo.save(donation);
                
                // Update treatment raised amount if status changed to/from COMPLETED
                if (donation.getTreatment() != null && 
                    donation.getDonationType() == Donation.DonationType.MONEY) {
                    if ((oldStatus != Donation.PaymentStatus.COMPLETED && 
                         newStatus == Donation.PaymentStatus.COMPLETED) ||
                        (oldStatus == Donation.PaymentStatus.COMPLETED && 
                         newStatus != Donation.PaymentStatus.COMPLETED)) {
                        treatmentService.updateRaisedAmount(donation.getTreatment().getTreatmentId());
                    }
                }
                
                return updatedDonation;
            })
            .orElseThrow(() -> new RuntimeException("Donation not found with id: " + donationId));
    }
    
    @Transactional
    public Donation updateDonation(Integer id, Donation updatedDonation) {
        return donationRepo.findById(id)
            .map(donation -> {
                if (updatedDonation.getAmount() != null) {
                    donation.setAmount(updatedDonation.getAmount());
                }
                if (updatedDonation.getNotes() != null) {
                    donation.setNotes(updatedDonation.getNotes());
                }
                if (updatedDonation.getTreatment() != null) {
                    donation.setTreatment(updatedDonation.getTreatment());
                }
                if (updatedDonation.getEquipment() != null) {
                    donation.setEquipment(updatedDonation.getEquipment());
                }
                if (updatedDonation.getSupply() != null) {
                    donation.setSupply(updatedDonation.getSupply());
                }
                return donationRepo.save(donation);
            })
            .orElseThrow(() -> new RuntimeException("Donation not found with id: " + id));
    }
    
    @Transactional
    public void deleteDonation(Integer id) {
        donationRepo.deleteById(id);
    }
}

