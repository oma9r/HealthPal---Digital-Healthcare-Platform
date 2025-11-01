package org.example.healthcare.repository;

import org.example.healthcare.model.Doctor;
import org.example.healthcare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepo extends JpaRepository<Doctor, Integer> {
    Doctor findByDoctorId(int id);
    Doctor findByUser_UserId(int userId);
    List<Doctor> findByUser_verifiedTrue();
    List<Doctor> findBySpecialtyIgnoreCase(String specialty);
    Doctor findByUser(User user);
    @Query("SELECT d FROM Doctor d WHERE LOWER(d.bio) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(d.specialty) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Doctor> searchDoctors(String keyword);



    List<Doctor> findByUser_verifiedFalse();}
