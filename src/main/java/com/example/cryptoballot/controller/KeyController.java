// src/main/java/com/example/cryptoballot/controller/KeyController.java
package com.example.cryptoballot.controller;

import com.example.cryptoballot.security.KeyManager;
import com.example.cryptoballot.util.Paillier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/key")
@CrossOrigin(origins = "http://localhost:8080")
public class KeyController {

    @PostMapping
    public ResponseEntity<String> setKey(@RequestBody KeyRequest keyRequest) {
        try {
            Paillier paillier;
            if (keyRequest.getMode().equalsIgnoreCase("generate")) {
                // Generate a new key with default 512 bits and certainty 64.
                paillier = new Paillier(512, 64);
            } else if (keyRequest.getMode().equalsIgnoreCase("custom")) {
                // Use custom parameters provided by the user (assumed in base 10).
                BigInteger n = new BigInteger(keyRequest.getN());
                BigInteger g = new BigInteger(keyRequest.getG());
                BigInteger lambda = new BigInteger(keyRequest.getLambda());
                // Create a new Paillier instance and assign custom values.
                paillier = new Paillier();
                paillier.n = n;
                paillier.g = g;
                paillier.lambda = lambda;
                paillier.nsquare = n.multiply(n);
            } else {
                return ResponseEntity.badRequest().body("Invalid mode. Use 'generate' or 'custom'.");
            }
            KeyManager.setPaillier(paillier);
            return ResponseEntity.ok("Key set successfully. Current key modulus (n): " + paillier.n.toString());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error setting key: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getKey() {
        Paillier p = KeyManager.getPaillier();
        if (p == null) {
            return ResponseEntity.ok("No key set yet.");
        } else {
            KeyResponse response = new KeyResponse(p.n.toString(), p.g.toString(), p.lambda.toString());
            return ResponseEntity.ok(response);
        }
    }

    // DTO classes for requests and responses.
    public static class KeyRequest {
        private String mode; // "generate" or "custom"
        private String n;
        private String g;
        private String lambda;

        public String getMode() { return mode; }
        public void setMode(String mode) { this.mode = mode; }
        public String getN() { return n; }
        public void setN(String n) { this.n = n; }
        public String getG() { return g; }
        public void setG(String g) { this.g = g; }
        public String getLambda() { return lambda; }
        public void setLambda(String lambda) { this.lambda = lambda; }
    }

    public static class KeyResponse {
        private String n;
        private String g;
        private String lambda;

        public KeyResponse(String n, String g, String lambda) {
            this.n = n;
            this.g = g;
            this.lambda = lambda;
        }

        public String getN() { return n; }
        public String getG() { return g; }
        public String getLambda() { return lambda; }
    }
}
