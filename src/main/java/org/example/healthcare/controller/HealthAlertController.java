package org.example.healthcare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.healthcare.service.PublicHealthAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/health-alerts")
@Tag(name = "Health Alerts", description = "Public health alerts and notifications")
public class HealthAlertController {
    
    @Autowired
    private PublicHealthAlertService alertService;
    
    @GetMapping
    @Operation(summary = "Get active health alerts", description = "Retrieve all active public health alerts")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN', 'NGO', 'DONOR')")
    public ResponseEntity<List<Map<String, Object>>> getActiveAlerts() {
        return ResponseEntity.ok(alertService.getActiveAlerts());
    }
    
    @PostMapping
    @Operation(summary = "Create health alert", description = "Create a new public health alert (Admin only)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> createAlert(@RequestBody Map<String, String> request) {
        String title = request.get("title");
        String description = request.get("description");
        String severity = request.getOrDefault("severity", "INFO");
        
        Map<String, Object> alert = alertService.createAlert(title, description, severity);
        return ResponseEntity.status(HttpStatus.CREATED).body(alert);
    }
}

