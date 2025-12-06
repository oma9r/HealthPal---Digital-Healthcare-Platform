package org.example.healthcare.controller;

import org.example.healthcare.model.ConsultationMessage;
import org.example.healthcare.model.User;
import org.example.healthcare.repository.UserRepo;
import org.example.healthcare.service.ConsultationMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/consultations/{consultationId}/messages")
public class ConsultationMessageController {
    
    @Autowired
    private ConsultationMessageService messageService;
    
    @Autowired
    private UserRepo userRepo;
    
    @GetMapping
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN')")
    public ResponseEntity<List<ConsultationMessage>> getMessages(
            @PathVariable Integer consultationId) {
        return ResponseEntity.ok(messageService.getMessagesByConsultationId(consultationId));
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN')")
    public ResponseEntity<ConsultationMessage> sendMessage(
            @PathVariable Integer consultationId,
            @RequestBody Map<String, String> request,
            Authentication auth) {
        String email = auth.getName();
        User user = userRepo.findByEmail(email);
        
        String messageText = request.get("messageText");
        String attachmentUrl = request.get("attachmentUrl");
        
        ConsultationMessage message = messageService.sendMessage(
            consultationId, 
            user.getUserId(), 
            messageText, 
            attachmentUrl
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }
    
    @GetMapping("/{messageId}")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN')")
    public ResponseEntity<ConsultationMessage> getMessageById(
            @PathVariable Integer consultationId,
            @PathVariable Integer messageId) {
        return messageService.getMessageById(messageId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{messageId}")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN')")
    public ResponseEntity<ConsultationMessage> updateMessage(
            @PathVariable Integer consultationId,
            @PathVariable Integer messageId,
            @RequestBody ConsultationMessage message,
            Authentication auth) {
        String email = auth.getName();
        User user = userRepo.findByEmail(email);
        
        // Verify sender is the one updating
        return messageService.getMessageById(messageId)
            .map(m -> {
                if (!m.getSender().getUserId().equals(user.getUserId()) && !user.hasRole("ADMIN")) {
                    return ResponseEntity.<ConsultationMessage>status(HttpStatus.FORBIDDEN).build();
                }
                return ResponseEntity.ok(messageService.updateMessage(messageId, message));
            })
            .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{messageId}")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN')")
    public ResponseEntity<Void> deleteMessage(
            @PathVariable Integer consultationId,
            @PathVariable Integer messageId,
            Authentication auth) {
        String email = auth.getName();
        User user = userRepo.findByEmail(email);
        
        // Verify sender is the one deleting
        return messageService.getMessageById(messageId)
            .map(m -> {
                if (!m.getSender().getUserId().equals(user.getUserId()) && !user.hasRole("ADMIN")) {
                    return ResponseEntity.<Void>status(HttpStatus.FORBIDDEN).build();
                }
                messageService.deleteMessage(messageId);
                return ResponseEntity.noContent().<Void>build();
            })
            .orElse(ResponseEntity.notFound().build());
    }
}

