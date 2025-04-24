package com.example.cryptoballot.controller;

import com.example.cryptoballot.model.Vote;
import com.example.cryptoballot.repository.VoteRepository;
import com.example.cryptoballot.security.KeyManager;
import com.example.cryptoballot.util.Paillier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/crypto/aggregation")
@CrossOrigin(origins = "http://localhost:3000")
public class AggregationController {


    @Autowired
    private VoteRepository voteRepository;

    // Example: Encryption endpoint remains the same (it can still use a local instance)
    @GetMapping("/encrypt")
    public ResponseEntity<String> encrypt(@RequestParam String value) {
        // Use a temporary Paillier instance for encryption if desired, or you can also use the persisted key.
        Paillier tempPaillier = KeyManager.getPaillier();
        if (tempPaillier == null) {
            return ResponseEntity.badRequest().body("Error: Paillier key not set. Please set key using the key management form.");
        }
        try {
            BigInteger m = new BigInteger(value);
            BigInteger c = tempPaillier.encrypt(m);
            String encoded = c.toString(16); // hex encoding
            return ResponseEntity.ok(encoded);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Encryption error: " + ex.getMessage());
        }
    }

    // Decryption endpoint that checks if the key is set.
    @GetMapping("/decrypt")
    public ResponseEntity<String> decrypt(@RequestParam String ciphertext) {
        Paillier keyInstance = KeyManager.getPaillier();
        if (keyInstance == null) {
            return ResponseEntity.badRequest().body("Error: Paillier key not set. Please set key using the key management form.");
        }
        try {
            BigInteger c = new BigInteger(ciphertext, 16);
            BigInteger m = keyInstance.decrypt(c);
            return ResponseEntity.ok(m.toString());
        } catch (NumberFormatException ex) {
            return ResponseEntity.badRequest().body("Invalid ciphertext: " + ex.getMessage());
        }
    }

    // Aggregation endpoint that groups votes by candidate.
    @GetMapping("/aggregateVotesByCandidate")
    public ResponseEntity<?> aggregateVotesByCandidate() {
        Paillier keyInstance = KeyManager.getPaillier();
        if (keyInstance == null) {
            return ResponseEntity.badRequest().body("Error: Paillier key not set. Please set key using the key management form.");
        }
        List<Vote> votes = voteRepository.findAll();
        if (votes.isEmpty()) {
            return ResponseEntity.ok("No votes found.");
        }
        Map<String, List<Vote>> grouped = votes.stream()
                .collect(Collectors.groupingBy(Vote::getCandidate));
        Map<String, String> results = new HashMap<>();
        for (Map.Entry<String, List<Vote>> entry : grouped.entrySet()) {
            String candidate = entry.getKey();
            List<Vote> candidateVotes = entry.getValue();
            BigInteger aggregate = BigInteger.ONE;
            for (Vote v : candidateVotes) {
                String cipherStr = v.getEncryptedVote().trim();
                if (cipherStr.isEmpty()) continue;
                try {
                    BigInteger num = new BigInteger(cipherStr, 16);
                    aggregate = aggregate.multiply(num).mod(keyInstance.nsquare);
                } catch (NumberFormatException ex) {
                    return ResponseEntity.badRequest().body(
                        "Invalid vote ciphertext for candidate '" + candidate + "': '" + cipherStr + "'");
                }
            }
            BigInteger totalVotes = keyInstance.decrypt(aggregate);
            results.put(candidate, totalVotes.toString());
        }
        return ResponseEntity.ok(results);
    }
}
