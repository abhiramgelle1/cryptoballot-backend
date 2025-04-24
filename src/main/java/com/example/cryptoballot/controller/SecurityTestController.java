package com.example.cryptoballot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/security")
@CrossOrigin(origins = "http://localhost:3000")
public class SecurityTestController {
    
    @GetMapping("/testRate")
    public ResponseEntity<String> testRateLimit() {
        return ResponseEntity.ok("Request Successful");
    }
}
