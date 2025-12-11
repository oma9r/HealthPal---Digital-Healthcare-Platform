package org.example.healthcare.controller;

import org.example.healthcare.model.Equipment;
import org.example.healthcare.model.NGO;
import org.example.healthcare.model.User;
import org.example.healthcare.repository.NGORepo;
import org.example.healthcare.repository.UserRepo;
import org.example.healthcare.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {
    
    @Autowired
    private EquipmentService equipmentService;
    
    @Autowired
    private UserRepo userRepo;
    
    @Autowired
    private NGORepo ngoRepo;
    
    @GetMapping
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN', 'NGO', 'DONOR')")
    public ResponseEntity<List<Equipment>> getAllEquipment(
            @RequestParam(required = false) Boolean available) {
        if (available != null && available) {
            return ResponseEntity.ok(equipmentService.getAvailableEquipment());
        }
        return ResponseEntity.ok(equipmentService.getAllEquipment());
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN', 'NGO', 'DONOR')")
    public ResponseEntity<Equipment> getEquipmentById(@PathVariable Integer id) {
        return equipmentService.getEquipmentById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN', 'NGO', 'DONOR')")
    public ResponseEntity<List<Equipment>> searchEquipment(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String condition) {
        return ResponseEntity.ok(equipmentService.searchAvailableEquipment(location, condition));
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('NGO', 'ADMIN')")
    public ResponseEntity<Equipment> createEquipment(
            @RequestBody Equipment equipment,
            Authentication auth) {
        String email = auth.getName();
        User user = userRepo.findByEmail(email);
        
        // Set NGO if user is NGO and not admin
        if (user.hasRole("NGO") && !user.hasRole("ADMIN")) {
            ngoRepo.findByUser_UserId(user.getUserId())
                .ifPresent(equipment::setNgo);
        }
        
        Equipment created = equipmentService.createEquipment(equipment);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('NGO', 'ADMIN')")
    public ResponseEntity<Equipment> updateEquipment(
            @PathVariable Integer id,
            @RequestBody Equipment equipment,
            Authentication auth) {
        String email = auth.getName();
        User user = userRepo.findByEmail(email);
        
        // Verify ownership if not admin
        if (!user.hasRole("ADMIN")) {
            return equipmentService.getEquipmentById(id)
                .<ResponseEntity<Equipment>>map(e -> {
                    if (e.getNgo() != null && e.getNgo().getUser().getUserId() != user.getUserId()) {
                        return ResponseEntity.<Equipment>status(HttpStatus.FORBIDDEN).build();
                    }
                    return ResponseEntity.ok(equipmentService.updateEquipment(id, equipment));
                })
                .orElse(ResponseEntity.<Equipment>notFound().build());
        }
        
        return ResponseEntity.ok(equipmentService.updateEquipment(id, equipment));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('NGO', 'ADMIN')")
    public ResponseEntity<Void> deleteEquipment(@PathVariable Integer id, Authentication auth) {
        String email = auth.getName();
        User user = userRepo.findByEmail(email);
        
        if (!user.hasRole("ADMIN")) {
            return equipmentService.getEquipmentById(id)
                .<ResponseEntity<Void>>map(e -> {
                    if (e.getNgo() != null && e.getNgo().getUser().getUserId() != user.getUserId()) {
                        return ResponseEntity.<Void>status(HttpStatus.FORBIDDEN).build();
                    }
                    equipmentService.deleteEquipment(id);
                    return ResponseEntity.<Void>noContent().build();
                })
                .orElse(ResponseEntity.<Void>notFound().build());
        }
        
        equipmentService.deleteEquipment(id);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{id}/toggle-availability")
    @PreAuthorize("hasAnyRole('NGO', 'ADMIN')")
    public ResponseEntity<Equipment> toggleAvailability(@PathVariable Integer id) {
        return ResponseEntity.ok(equipmentService.toggleAvailability(id));
    }
}

