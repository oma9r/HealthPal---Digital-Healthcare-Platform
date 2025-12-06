package org.example.healthcare.repository;

import org.example.healthcare.model.Donor;
import org.example.healthcare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DonorRepo extends JpaRepository<Donor, Integer> {
    Optional<Donor> findByDonorId(Integer donorId);
    Optional<Donor> findByUserUserId(Integer userId);
    Optional<Donor> findByUser(User user);
}
