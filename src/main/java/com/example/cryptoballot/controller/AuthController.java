package com.example.cryptoballot.controller;

import com.example.cryptoballot.dto.RegistrationRequest;
import com.example.cryptoballot.dto.LoginRequest;
import com.example.cryptoballot.model.Voter;
import com.example.cryptoballot.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private AuthService authService;

    // Registration endpoint: registers a new voter using username and password
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegistrationRequest req) {
        try {
            Voter voter = authService.registerVoter(req.getUsername(), req.getPassword());
            return ResponseEntity.ok("Voter registered: " + voter.getUsername() + ". You are now logged in.");
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // Login endpoint: authenticates using username and password
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest req) {
        try {
            boolean valid = authService.login(req.getUsername(), req.getPassword());
            if (valid) {
                return ResponseEntity.ok("Login successful!");
            } else {
                return ResponseEntity.status(401).body("Invalid username or password.");
            }
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
