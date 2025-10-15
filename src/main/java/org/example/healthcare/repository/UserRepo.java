package org.example.healthcare.repository;

import org.example.healthcare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    User findByFullName(String fullName);
    User findByEmail(String email);
    User findByPhoneNumber(String phoneNumber);
    List<User> findByRole(String role);



    List<User> findByVerifiedTrue();
    List<User> findByVerifiedFalse();
    @Query("SELECT u FROM User u WHERE LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) ")
    List<User> searchUsers(String keyword);
}
