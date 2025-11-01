package org.example.healthcare.repository;

import org.example.healthcare.model.Consultation;
import org.example.healthcare.model.Doctor;
import org.example.healthcare.model.Patient;
import org.example.healthcare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConsultationRepo extends JpaRepository<Consultation, Integer> {
    Consultation findById(long id);







    List<Consultation> findByPatient(Patient patient);
    List<Consultation> findByDoctor(Doctor doctor);
}
