package org.example.healthcare.repository;

import org.example.healthcare.model.Donation;
import org.example.healthcare.model.Donor;
import org.example.healthcare.model.NGO;
import org.example.healthcare.model.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DonationRepo extends JpaRepository<Donation, Integer> {
    List<Donation> findByDonor(Donor donor);
    List<Donation> findByNgo(NGO ngo);
    List<Donation> findByTreatment(Treatment treatment);
    List<Donation> findByDonationType(Donation.DonationType donationType);
    List<Donation> findByPaymentStatus(Donation.PaymentStatus paymentStatus);
    
    @Query("SELECT COALESCE(SUM(d.amount), 0) FROM Donation d WHERE d.treatment.treatmentId = :treatmentId AND d.paymentStatus = 'COMPLETED'")
    BigDecimal getTotalDonatedByTreatmentId(@Param("treatmentId") Integer treatmentId);
    
    @Query("SELECT d FROM Donation d WHERE d.treatment.treatmentId = :treatmentId")
    List<Donation> findAllByTreatmentId(@Param("treatmentId") Integer treatmentId);
    
    @Query("SELECT d FROM Donation d WHERE d.donor.donorId = :donorId ORDER BY d.dateDonated DESC")
    List<Donation> findAllByDonorIdOrderByDateDesc(@Param("donorId") Integer donorId);
}

