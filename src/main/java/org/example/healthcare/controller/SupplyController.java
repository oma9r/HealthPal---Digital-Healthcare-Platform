package org.example.healthcare.controller;

import org.example.healthcare.model.NGO;
import org.example.healthcare.model.Supply;
import org.example.healthcare.model.User;
import org.example.healthcare.repository.NGORepo;
import org.example.healthcare.repository.UserRepo;
import org.example.healthcare.service.SupplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/supplies")
public class SupplyController {
    
    @Autowired
    private SupplyService supplyService;
    
    @Autowired
    private UserRepo userRepo;
    
    @Autowired
    private NGORepo ngoRepo;
    
    @GetMapping
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN', 'NGO', 'DONOR')")
    public ResponseEntity<List<Supply>> getAllSupplies() {
        return ResponseEntity.ok(supplyService.getAllSupplies());
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN', 'NGO', 'DONOR')")
    public ResponseEntity<Supply> getSupplyById(@PathVariable Integer id) {
        return supplyService.getSupplyById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/category/{category}")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN', 'NGO', 'DONOR')")
    public ResponseEntity<List<Supply>> getSuppliesByCategory(@PathVariable String category) {
        return ResponseEntity.ok(supplyService.getAvailableSuppliesByCategory(category));
    }
    
    @GetMapping("/expiring")
    @PreAuthorize("hasAnyRole('ADMIN', 'NGO')")
    public ResponseEntity<List<Supply>> getExpiringSupplies(
            @RequestParam(defaultValue = "30") int daysAhead) {
        return ResponseEntity.ok(supplyService.getExpiringSupplies(daysAhead));
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('NGO', 'ADMIN')")
    public ResponseEntity<Supply> createSupply(
            @RequestBody Supply supply,
            Authentication auth) {
        String email = auth.getName();
        User user = userRepo.findByEmail(email);
        
        // Set NGO if user is NGO and not admin
        if (user.hasRole("NGO") && !user.hasRole("ADMIN")) {
            ngoRepo.findByUser_UserId(user.getUserId())
                .ifPresent(supply::setNgo);
        }
        
        Supply created = supplyService.createSupply(supply);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('NGO', 'ADMIN')")
    public ResponseEntity<Supply> updateSupply(
            @PathVariable Integer id,
            @RequestBody Supply supply,
            Authentication auth) {
        String email = auth.getName();
        User user = userRepo.findByEmail(email);
        
        // Verify ownership if not admin
        if (!user.hasRole("ADMIN")) {
            return supplyService.getSupplyById(id)
                .map(s -> {
                    if (s.getNgo() != null && !s.getNgo().getUser().getUserId().equals(user.getUserId())) {
                        return ResponseEntity.<Supply>status(HttpStatus.FORBIDDEN).build();
                    }
                    return ResponseEntity.ok(supplyService.updateSupply(id, supply));
                })
                .orElse(ResponseEntity.notFound().build());
        }
        
        return ResponseEntity.ok(supplyService.updateSupply(id, supply));
    }
    
    @PutMapping("/{id}/quantity")
    @PreAuthorize("hasAnyRole('NGO', 'ADMIN')")
    public ResponseEntity<Supply> updateQuantity(
            @PathVariable Integer id,
            @RequestParam Integer change) {
        return ResponseEntity.ok(supplyService.updateQuantity(id, change));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('NGO', 'ADMIN')")
    public ResponseEntity<Void> deleteSupply(@PathVariable Integer id, Authentication auth) {
        String email = auth.getName();
        User user = userRepo.findByEmail(email);
        
        if (!user.hasRole("ADMIN")) {
            return supplyService.getSupplyById(id)
                .map(s -> {
                    if (s.getNgo() != null && !s.getNgo().getUser().getUserId().equals(user.getUserId())) {
                        return ResponseEntity.<Void>status(HttpStatus.FORBIDDEN).build();
                    }
                    supplyService.deleteSupply(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
        }
        
        supplyService.deleteSupply(id);
        return ResponseEntity.noContent().build();
    }
}

