package org.example.healthcare.controller;

import org.example.healthcare.model.Equipment;
import org.example.healthcare.model.Supply;
import org.example.healthcare.service.EquipmentService;
import org.example.healthcare.service.SupplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    
    @Autowired
    private EquipmentService equipmentService;
    
    @Autowired
    private SupplyService supplyService;
    
    @GetMapping
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN', 'NGO', 'DONOR')")
    public ResponseEntity<Map<String, Object>> getInventory(
            @RequestParam(required = false) Boolean available) {
        List<Equipment> equipment;
        if (available != null && available) {
            equipment = equipmentService.getAvailableEquipment();
        } else {
            equipment = equipmentService.getAllEquipment();
        }
        
        List<Supply> supplies = supplyService.getAllSupplies();
        
        Map<String, Object> inventory = new HashMap<>();
        inventory.put("equipment", equipment);
        inventory.put("supplies", supplies);
        inventory.put("totalEquipment", equipment.size());
        inventory.put("totalSupplies", supplies.size());
        
        return ResponseEntity.ok(inventory);
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN', 'NGO', 'DONOR')")
    public ResponseEntity<Map<String, Object>> searchInventory(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String condition,
            @RequestParam(required = false) String category) {
        
        List<Equipment> equipment = equipmentService.searchAvailableEquipment(location, condition);
        List<Supply> supplies = new ArrayList<>();
        
        if (category != null) {
            supplies = supplyService.getAvailableSuppliesByCategory(category);
        } else {
            supplies = supplyService.getAllSupplies();
        }
        
        Map<String, Object> results = new HashMap<>();
        results.put("equipment", equipment);
        results.put("supplies", supplies);
        
        return ResponseEntity.ok(results);
    }
}
