package org.example.healthcare.service;

import org.example.healthcare.model.User;
import org.example.healthcare.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }
    public User getUserById(int id) {
        return userRepo.findById(id).get();
    }
    public List<User> getAllVerifiedUsers() {
        return userRepo.findByVerifiedTrue();
    }
    public List<User> getAllUnverifiedUsers() {
        return userRepo.findByVerifiedFalse();
    }
    public List<User> getByRole(String role) {
        return userRepo.findByRole(role);
    }
    public List<User> SearchUser(String keyword) {
        return userRepo.searchUsers(keyword);
    }
    public User getByEmail(String email) {
        return userRepo.findByEmail(email);
    }
    public User getByPhone(String phone) {
        return userRepo.findByPhoneNumber(phone);
    }
    public User getByFullName(String name) {
        return userRepo.findByFullName(name);
    }
    public User addUser(User user) {
        return userRepo.save(user);
    }



}
