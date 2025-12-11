package org.example.healthcare.repository;

import org.example.healthcare.model.MedicalRecord;
import org.example.healthcare.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalRecordRepo extends JpaRepository<MedicalRecord, Integer> {
    List<MedicalRecord> findByPatient(Patient patient);
    List<MedicalRecord> findByPatient_PatientId(Integer patientId);
    List<MedicalRecord> findByRecordType(MedicalRecord.RecordType recordType);
    List<MedicalRecord> findByPatient_PatientIdAndRecordType(Integer patientId, MedicalRecord.RecordType recordType);
}

