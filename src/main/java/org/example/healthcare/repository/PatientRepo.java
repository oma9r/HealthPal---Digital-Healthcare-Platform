package org.example.healthcare.repository;

import org.example.healthcare.model.Patient;
import org.example.healthcare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepo  extends JpaRepository<Patient, Integer> {
    Patient findByPatientId(int patientId);
    Patient findByUserUserId(int patientName);
    Patient findByUser(User user);


}
