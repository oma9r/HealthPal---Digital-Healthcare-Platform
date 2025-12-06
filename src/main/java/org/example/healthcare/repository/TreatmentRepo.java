package org.example.healthcare.repository;

import org.example.healthcare.model.Patient;
import org.example.healthcare.model.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreatmentRepo extends JpaRepository<Treatment, Integer> {
    List<Treatment> findByPatient(Patient patient);
    List<Treatment> findByPatient_PatientId(Integer patientId);
    List<Treatment> findByStatus(Treatment.TreatmentStatus status);
    List<Treatment> findByTreatmentType(Treatment.TreatmentType treatmentType);
    List<Treatment> findByStatusAndTreatmentType(Treatment.TreatmentStatus status, Treatment.TreatmentType treatmentType);
    
    @Query("SELECT t FROM Treatment t WHERE t.status = 'ACTIVE' ORDER BY t.startDate DESC")
    List<Treatment> findAllActiveTreatments();
    
    @Query("SELECT t FROM Treatment t WHERE t.patient.patientId = :patientId AND t.status = :status")
    List<Treatment> findByPatientIdAndStatus(@Param("patientId") Integer patientId, @Param("status") Treatment.TreatmentStatus status);
}

