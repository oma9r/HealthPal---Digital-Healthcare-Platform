package org.example.healthcare.controller;

import org.example.healthcare.model.Donation;
import org.example.healthcare.model.Donor;
import org.example.healthcare.model.User;
import org.example.healthcare.repository.DonorRepo;
import org.example.healthcare.repository.UserRepo;
import org.example.healthcare.service.DonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/donations")
public class DonationController {
    
    @Autowired
    private DonationService donationService;
    
    @Autowired
    private DonorRepo donorRepo;
    
    @Autowired
    private UserRepo userRepo;
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DONOR', 'NGO')")
    public ResponseEntity<List<Donation>> getAllDonations(
            @RequestParam(required = false) Integer donorId,
            @RequestParam(required = false) Integer treatmentId,
            @RequestParam(required = false) String status,
            Authentication auth) {
        String email = auth.getName();
        User user = userRepo.findByEmail(email);
        
        List<Donation> donations;
        if (donorId != null) {
            donations = donationService.getDonationsByDonorId(donorId);
        } else if (treatmentId != null) {
            donations = donationService.getDonationsByTreatmentId(treatmentId);
        } else if (status != null) {
            try {
                Donation.PaymentStatus paymentStatus = Donation.PaymentStatus.valueOf(status.toUpperCase());
                donations = donationService.getDonationsByStatus(paymentStatus);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
        } else {
            // If donor, only show their donations
            if (user.hasRole("DONOR")) {
                Donor donor = donorRepo.findByUserUserId(user.getUserId()).orElse(null);
                if (donor != null) {
                    donations = donationService.getDonationsByDonorId(donor.getDonorId());
                } else {
                    return ResponseEntity.ok(List.of());
                }
            } else {
                donations = donationService.getAllDonations();
            }
        }
        
        return ResponseEntity.ok(donations);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DONOR', 'NGO')")
    public ResponseEntity<Donation> getDonationById(@PathVariable Integer id) {
        return donationService.getDonationById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('DONOR', 'NGO', 'ADMIN')")
    public ResponseEntity<Donation> createDonation(
            @RequestBody Donation donation,
            Authentication auth) {
        String email = auth.getName();
        User user = userRepo.findByEmail(email);
        
        // Set donor if user is a donor
        if (user.hasRole("DONOR")) {
            donorRepo.findByUserUserId(user.getUserId())
                .ifPresent(donation::setDonor);
        }
        
        Donation created = donationService.createDonation(donation);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'NGO')")
    public ResponseEntity<Donation> updatePaymentStatus(
            @PathVariable Integer id,
            @RequestBody Map<String, String> request) {
        String status = request.get("status");
        try {
            Donation.PaymentStatus paymentStatus = Donation.PaymentStatus.valueOf(status.toUpperCase());
            return ResponseEntity.ok(donationService.updatePaymentStatus(id, paymentStatus));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/transparency")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Donation>> getTransparencyDashboard(
            @RequestParam(required = false) Integer treatmentId) {
        List<Donation> donations;
        if (treatmentId != null) {
            donations = donationService.getDonationsByTreatmentId(treatmentId);
        } else {
            donations = donationService.getAllDonations();
        }
        return ResponseEntity.ok(donations);
    }
}

