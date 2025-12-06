package org.example.healthcare.dto;

import lombok.Data;
import org.example.healthcare.model.Treatment;

import java.math.BigDecimal;
import java.sql.Date;

@Data
public class TreatmentResponse {
    private Integer treatmentId;
    private Integer patientId;
    private Treatment.TreatmentType treatmentType;
    private String description;
    private BigDecimal goalAmount;
    private BigDecimal raisedAmount;
    private BigDecimal progressPercent;
    private Date startDate;
    private Date endDate;
    private Treatment.TreatmentStatus status;
}

