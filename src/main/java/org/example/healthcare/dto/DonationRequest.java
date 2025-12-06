package org.example.healthcare.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.example.healthcare.model.Donation;

import java.math.BigDecimal;

@Data
public class DonationRequest {
    @NotNull(message = "Donation type is required")
    private Donation.DonationType donationType;
    
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0", groups = MoneyDonation.class)
    private BigDecimal amount;
    
    private Integer treatmentId;
    private Integer equipmentId;
    private Integer supplyId;
    private Integer ngoId;
    
    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;
    
    // Validation group for money donations
    public interface MoneyDonation {}
    
    @AssertTrue(message = "Money donations must have amount", groups = MoneyDonation.class)
    private boolean isValidMoneyDonation() {
        return donationType != Donation.DonationType.MONEY || amount != null;
    }
}

