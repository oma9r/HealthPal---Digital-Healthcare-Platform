package org.example.healthcare.controller;


import org.example.healthcare.repository.UserRepo;
import org.example.healthcare.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.example.healthcare.model.User;
@RestController
@RequestMapping("/api/auth/")
public class AuthController {

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;





    @PostMapping("/register")
    public User register(@RequestBody User user) {
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        return userRepository.save(user);

    }

    @PostMapping("/login")
    public String login(@RequestBody User request) {
        User user = userRepository.findByEmail(request.getEmail());
        if(user == null) {
            return "Invalid email or password";
        }
        if (!passwordEncoder.matches(request.getPasswordHash(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");

        }

        return jwtUtil.generateToken(user.getEmail(), user.getRole().name())+"\t"+ user.getRole().name();
    }
}
