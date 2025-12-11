package org.example.healthcare.repository;

import org.example.healthcare.model.NGO;
import org.example.healthcare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NGORepo extends JpaRepository<NGO, Integer> {
    List<NGO> findByVerifiedTrue();
    List<NGO> findByVerifiedFalse();
    Optional<NGO> findByUser(User user);
    Optional<NGO> findByUser_UserId(Integer userId);
    List<NGO> findByNameContainingIgnoreCase(String name);
}

