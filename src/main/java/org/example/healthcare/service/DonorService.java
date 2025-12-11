package org.example.healthcare.service;

import org.example.healthcare.model.Donor;
import org.example.healthcare.model.Donation;
import org.example.healthcare.model.User;
import org.example.healthcare.repository.DonorRepo;
import org.example.healthcare.repository.DonationRepo;
import org.example.healthcare.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DonorService {
    
    @Autowired
    private DonorRepo donorRepo;
    
    @Autowired
    private DonationRepo donationRepo;
    
    @Autowired
    private UserRepo userRepo;
    
    public List<Donor> getAllDonors() {
        return donorRepo.findAll();
    }
    
    public Optional<Donor> getDonorById(Integer id) {
        return donorRepo.findById(id);
    }
    
    public Optional<Donor> getDonorByUserId(Integer userId) {
        return donorRepo.findByUserUserId(userId);
    }
    
    public List<Donation> getDonationHistory(Integer donorId) {
        return donorRepo.findById(donorId)
            .map(donationRepo::findByDonor)
            .orElse(List.of());
    }
    
    @Transactional
    public Donor createDonor(Donor donor) {
        return donorRepo.save(donor);
    }
    
    @Transactional
    public Donor updateDonor(Integer id, Donor updatedDonor) {
        return donorRepo.findById(id)
            .map(donor -> {
                if (updatedDonor.getOrganization() != null) {
                    donor.setOrganization(updatedDonor.getOrganization());
                }
                return donorRepo.save(donor);
            })
            .orElseThrow(() -> new RuntimeException("Donor not found with id: " + id));
    }
    
    @Transactional
    public void deleteDonor(Integer id) {
        donorRepo.deleteById(id);
    }
}
