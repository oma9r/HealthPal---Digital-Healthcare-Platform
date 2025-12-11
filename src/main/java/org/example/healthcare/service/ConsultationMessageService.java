package org.example.healthcare.service;

import org.example.healthcare.model.Consultation;
import org.example.healthcare.model.ConsultationMessage;
import org.example.healthcare.model.User;
import org.example.healthcare.repository.ConsultationMessageRepo;
import org.example.healthcare.repository.ConsultationRepo;
import org.example.healthcare.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ConsultationMessageService {
    
    @Autowired
    private ConsultationMessageRepo messageRepo;
    
    @Autowired
    private ConsultationRepo consultationRepo;
    
    @Autowired
    private UserRepo userRepo;
    
    public List<ConsultationMessage> getMessagesByConsultationId(Integer consultationId) {
        return messageRepo.findByConsultation_IdOrderByCreatedAtAsc(consultationId);
    }
    
    public Optional<ConsultationMessage> getMessageById(Integer id) {
        return messageRepo.findById(id);
    }
    
    @Transactional
    public ConsultationMessage createMessage(ConsultationMessage message) {
        // Verify consultation exists
        consultationRepo.findById(message.getConsultation().getId())
            .orElseThrow(() -> new RuntimeException("Consultation not found"));
        
        // Verify sender exists
        userRepo.findById(message.getSender().getUserId())
            .orElseThrow(() -> new RuntimeException("Sender user not found"));
        
        return messageRepo.save(message);
    }
    
    @Transactional
    public ConsultationMessage sendMessage(Integer consultationId, Integer senderId, String messageText, String attachmentUrl) {
        Consultation consultation = consultationRepo.findById(consultationId)
            .orElseThrow(() -> new RuntimeException("Consultation not found with id: " + consultationId));
        
        User sender = userRepo.findById(senderId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + senderId));
        
        ConsultationMessage message = ConsultationMessage.builder()
            .consultation(consultation)
            .sender(sender)
            .messageText(messageText)
            .attachmentUrl(attachmentUrl)
            .build();
        
        return messageRepo.save(message);
    }
    
    @Transactional
    public ConsultationMessage updateMessage(Integer id, ConsultationMessage updatedMessage) {
        return messageRepo.findById(id)
            .map(message -> {
                if (updatedMessage.getMessageText() != null) {
                    message.setMessageText(updatedMessage.getMessageText());
                }
                if (updatedMessage.getAttachmentUrl() != null) {
                    message.setAttachmentUrl(updatedMessage.getAttachmentUrl());
                }
                return messageRepo.save(message);
            })
            .orElseThrow(() -> new RuntimeException("Message not found with id: " + id));
    }
    
    @Transactional
    public void deleteMessage(Integer id) {
        messageRepo.deleteById(id);
    }
    
    @Transactional
    public void deleteMessagesByConsultationId(Integer consultationId) {
        messageRepo.deleteByConsultation_Id(consultationId);
    }
}

