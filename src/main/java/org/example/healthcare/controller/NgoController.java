package org.example.healthcare.controller;

import org.example.healthcare.model.NGO;
import org.example.healthcare.model.User;
import org.example.healthcare.repository.EquipmentRepo;
import org.example.healthcare.repository.NGORepo;
import org.example.healthcare.repository.SupplyRepo;
import org.example.healthcare.repository.UserRepo;
import org.example.healthcare.service.EquipmentService;
import org.example.healthcare.service.NGOService;
import org.example.healthcare.service.SupplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ngo")
public class NgoController {
    
    @Autowired
    private NGOService ngoService;
    
    @Autowired
    private EquipmentService equipmentService;
    
    @Autowired
    private SupplyService supplyService;
    
    @Autowired
    private UserRepo userRepo;
    
    @PostMapping("/register")
    public ResponseEntity<NGO> registerNGO(@RequestBody NGO ngo) {
        NGO created = ngoService.createNGO(ngo);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN', 'NGO', 'DONOR')")
    public ResponseEntity<List<NGO>> getAllNGOs(
            @RequestParam(required = false) Boolean verified) {
        if (verified != null && verified) {
            return ResponseEntity.ok(ngoService.getVerifiedNGOs());
        } else if (verified != null && !verified) {
            return ResponseEntity.ok(ngoService.getUnverifiedNGOs());
        }
        return ResponseEntity.ok(ngoService.getAllNGOs());
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN', 'NGO', 'DONOR')")
    public ResponseEntity<NGO> getNGOById(@PathVariable Integer id) {
        return ngoService.getNGOById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}/verify")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NGO> verifyNGO(@PathVariable Integer id) {
        return ResponseEntity.ok(ngoService.verifyNGO(id));
    }
    
    @PutMapping("/{id}/unverify")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NGO> unverifyNGO(@PathVariable Integer id) {
        return ResponseEntity.ok(ngoService.unverifyNGO(id));
    }
    
    @GetMapping("/{id}/equipment")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN', 'NGO', 'DONOR')")
    public ResponseEntity<List<org.example.healthcare.model.Equipment>> getNGOEquipment(@PathVariable Integer id) {
        return ResponseEntity.ok(equipmentService.getEquipmentByNgoId(id));
    }
    
    @GetMapping("/{id}/supplies")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN', 'NGO', 'DONOR')")
    public ResponseEntity<List<org.example.healthcare.model.Supply>> getNGOSupplies(@PathVariable Integer id) {
        return ResponseEntity.ok(supplyService.getSuppliesByNgoId(id));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('NGO', 'ADMIN')")
    public ResponseEntity<NGO> updateNGO(
            @PathVariable Integer id,
            @RequestBody NGO ngo,
            Authentication auth) {
        String email = auth.getName();
        User user = userRepo.findByEmail(email);
        
        // Verify ownership if not admin
        if (!user.hasRole("ADMIN")) {
            return ngoService.getNGOById(id)
                .map(n -> {
                    if (n.getUser().getUserId() != user.getUserId()) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).<NGO>build();
                    }
                    return ResponseEntity.ok(ngoService.updateNGO(id, ngo));
                })
                .orElse(ResponseEntity.<NGO>notFound().build());
        }
        
        return ResponseEntity.ok(ngoService.updateNGO(id, ngo));
    }
}
