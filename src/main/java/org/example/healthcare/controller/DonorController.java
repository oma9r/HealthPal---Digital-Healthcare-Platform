package org.example.healthcare.controller;

import org.example.healthcare.model.Donor;
import org.example.healthcare.model.Donation;
import org.example.healthcare.model.User;
import org.example.healthcare.repository.DonorRepo;
import org.example.healthcare.repository.UserRepo;
import org.example.healthcare.service.DonorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donors")
public class DonorController {
    
    @Autowired
    private DonorService donorService;
    
    @Autowired
    private UserRepo userRepo;
    
    @PostMapping("/register")
    public ResponseEntity<Donor> registerDonor(@RequestBody Donor donor) {
        Donor created = donorService.createDonor(donor);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @GetMapping("/profile")
    @PreAuthorize("hasRole('DONOR')")
    public ResponseEntity<Donor> getMyProfile(Authentication auth) {
        String email = auth.getName();
        User user = userRepo.findByEmail(email);
        
        return donorService.getDonorByUserId(user.getUserId())
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/profile")
    @PreAuthorize("hasRole('DONOR')")
    public ResponseEntity<Donor> updateMyProfile(
            @RequestBody Donor donor,
            Authentication auth) {
        String email = auth.getName();
        User user = userRepo.findByEmail(email);
        
        return donorService.getDonorByUserId(user.getUserId())
            .map(d -> {
                Donor updated = donorService.updateDonor(d.getDonorId(), donor);
                return ResponseEntity.ok(updated);
            })
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/donations")
    @PreAuthorize("hasRole('DONOR')")
    public ResponseEntity<List<Donation>> getMyDonations(Authentication auth) {
        String email = auth.getName();
        User user = userRepo.findByEmail(email);
        
        return donorService.getDonorByUserId(user.getUserId())
            .map(donor -> ResponseEntity.ok(donorService.getDonationHistory(donor.getDonorId())))
            .orElse(ResponseEntity.ok(List.of()));
    }
}
