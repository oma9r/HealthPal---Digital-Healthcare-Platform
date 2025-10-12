package org.example.healthcare.repository;

import org.example.healthcare.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepo extends JpaRepository<Doctor, Integer> {

    Doctor findByUser_UserId(int userId);
    List<Doctor> findByUser_verfiedTrue();
    List<Doctor> findBySpecialtyIgnoreCase(String specialty);

    @Query("SELECT d FROM Doctor d WHERE LOWER(d.user.full_name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(d.specialty) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Doctor> searchDoctors(String keyword);



    List<Doctor> findByUser_verfiedFalse();}
