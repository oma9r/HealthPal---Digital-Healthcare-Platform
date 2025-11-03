package org.example.healthcare.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class ConsultationRequest {
        private int doctorId;
        private LocalDateTime scheduledAt;
        private String mode;
        private boolean lowBandwidth;
        private String notes;

    }


