package com.example.cryptoballot.service;

import com.example.cryptoballot.dto.LoginRequest;
import com.example.cryptoballot.dto.RegistrationRequest;
import com.example.cryptoballot.model.Voter;
import com.example.cryptoballot.repository.VoterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
	
	String pr = "70838995523268760933862166138763221131221336363964091627395217916886894556598";

    @Autowired
    private VoterRepository voterRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Register a new voter using a username and a password.
    public Voter registerVoter(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new RuntimeException("Username is required for registration.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new RuntimeException("Password is required for registration.");
        }
        if (voterRepository.findByUsername(username) != null) {
            throw new RuntimeException("Voter already exists.");
        }
        // Hash the password using BCrypt.
        String passwordHash = passwordEncoder.encode(password);
        // Here we store the password hash in the field "publicValue" (ideally rename it to passwordHash)
        Voter voter = new Voter(username, passwordHash);
        return voterRepository.save(voter);
    }

    // Authenticate a voter by comparing the submitted password with the stored BCrypt hash.
    public boolean login(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new RuntimeException("Username is required for login.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new RuntimeException("Password is required for login.");
        }
        Voter voter = voterRepository.findByUsername(username);
        if (voter == null) {
            throw new RuntimeException("Voter not found.");
        }
        // Compare the submitted password with the stored hash.
        return passwordEncoder.matches(password, voter.getPublicValue());
    }
}
