package org.example.healthcare.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Entity
@Table(name = "consultation_messages")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ConsultationMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Integer messageId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consultation_id", referencedColumnName = "consultation_id")
    private Consultation consultation;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", referencedColumnName = "userId")
    private User sender;
    
    @Column(name = "message_text", columnDefinition = "TEXT")
    private String messageText;
    
    @Column(name = "attachment_url", columnDefinition = "TEXT")
    private String attachmentUrl;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

