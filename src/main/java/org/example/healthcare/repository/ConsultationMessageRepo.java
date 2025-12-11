package org.example.healthcare.repository;

import org.example.healthcare.model.Consultation;
import org.example.healthcare.model.ConsultationMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultationMessageRepo extends JpaRepository<ConsultationMessage, Integer> {
    List<ConsultationMessage> findByConsultation_IdOrderByCreatedAtAsc(Integer consultationId);
    List<ConsultationMessage> findByConsultation(Consultation consultation);
    void deleteByConsultation_Id(Integer consultationId);
}

