package org.example.healthcare.service;

import org.example.healthcare.model.Treatment;
import org.example.healthcare.repository.PatientRepo;
import org.example.healthcare.repository.TreatmentRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TreatmentServiceTest {
    
    @Mock
    private TreatmentRepo treatmentRepo;
    
    @Mock
    private PatientRepo patientRepo;
    
    @Mock
    private DonationService donationService;
    
    @InjectMocks
    private TreatmentService treatmentService;
    
    private Treatment testTreatment;
    
    @BeforeEach
    void setUp() {
        testTreatment = Treatment.builder()
            .treatmentId(1)
            .treatmentType(Treatment.TreatmentType.SURGERY)
            .description("Test treatment")
            .goalAmount(BigDecimal.valueOf(10000))
            .raisedAmount(BigDecimal.ZERO)
            .status(Treatment.TreatmentStatus.ACTIVE)
            .build();
    }
    
    @Test
    void testCreateTreatment() {
        when(treatmentRepo.save(any(Treatment.class))).thenReturn(testTreatment);
        
        Treatment created = treatmentService.createTreatment(testTreatment);
        
        assertNotNull(created);
        assertEquals(Treatment.TreatmentStatus.ACTIVE, created.getStatus());
        verify(treatmentRepo, times(1)).save(testTreatment);
    }
    
    @Test
    void testGetTreatmentById() {
        when(treatmentRepo.findById(1)).thenReturn(Optional.of(testTreatment));
        
        Optional<Treatment> result = treatmentService.getTreatmentById(1);
        
        assertTrue(result.isPresent());
        assertEquals(testTreatment.getTreatmentId(), result.get().getTreatmentId());
    }
    
    @Test
    void testUpdateRaisedAmount() {
        when(treatmentRepo.findById(1)).thenReturn(Optional.of(testTreatment));
        when(donationService.getTotalDonatedByTreatmentId(1)).thenReturn(BigDecimal.valueOf(5000));
        when(treatmentRepo.save(any(Treatment.class))).thenReturn(testTreatment);
        
        treatmentService.updateRaisedAmount(1);
        
        verify(treatmentRepo, times(1)).findById(1);
        verify(donationService, times(1)).getTotalDonatedByTreatmentId(1);
    }
}

