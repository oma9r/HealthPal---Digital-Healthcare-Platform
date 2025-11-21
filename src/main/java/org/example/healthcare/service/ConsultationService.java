package org.example.healthcare.service;

import lombok.RequiredArgsConstructor;
import org.example.healthcare.model.Consultation;
import org.example.healthcare.model.User;
import org.example.healthcare.repository.ConsultationRepo;
import org.example.healthcare.repository.DoctorRepo;
import org.example.healthcare.repository.PatientRepo;
import org.example.healthcare.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultationService {
    @Autowired
    private final ConsultationRepo consultationRepository;
    @Autowired
    private final UserRepo userRepository;
    @Autowired
    private final PatientRepo patientRepo;
    @Autowired
    private final DoctorRepo doctorRepo;

    public boolean canAccessConsultation(String userEmail, Consultation consultation) {
        User user = userRepository.findByEmail(userEmail);
        if (user.hasRole("ADMIN")) return true;

        // patient can access only their consultations
        if (user.hasRole("PATIENT") && consultation.getPatient().getUser().getUserId() == user.getUserId())
            return true;

        // doctor can access only assigned consultations
        if (user.hasRole("DOCTOR") && consultation.getDoctor().getUser().getUserId() == user.getUserId())
            return true;

        return false;
    }

    public List<Consultation> getConsultationsForUser(String email) {
        User user = userRepository.findByEmail(email);
        if (user.hasRole("PATIENT")) {

            return consultationRepository.findByPatient(patientRepo.findByUserUserId(user.getUserId()));
        } else if (user.hasRole("DOCTOR")) {
            return consultationRepository.findByDoctor(doctorRepo.findByUser(user));
        } else {
            return consultationRepository.findAll();
        }
    }

    public Consultation getConsultationById(int id) {
        return consultationRepository.findById(id);
    }

    public Consultation createConsultation(Consultation cons) {

       return consultationRepository.save(cons);
    }
}
