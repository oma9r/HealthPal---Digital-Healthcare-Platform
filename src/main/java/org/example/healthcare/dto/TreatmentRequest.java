package org.example.healthcare.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.example.healthcare.model.Treatment;

import java.math.BigDecimal;
import java.sql.Date;

@Data
public class TreatmentRequest {
    @NotNull(message = "Treatment type is required")
    private Treatment.TreatmentType treatmentType;
    
    @NotBlank(message = "Description is required")
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;
    
    @DecimalMin(value = "0.01", message = "Goal amount must be greater than 0")
    private BigDecimal goalAmount;
    
    @PastOrPresent(message = "Start date cannot be in the future")
    private Date startDate;
    
    private Date endDate;
}

