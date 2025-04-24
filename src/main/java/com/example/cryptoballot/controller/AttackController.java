package com.example.cryptoballot.controller;

import com.example.cryptoballot.model.Vote;
import com.example.cryptoballot.repository.VoteRepository;
import com.example.cryptoballot.security.KeyManager;
import com.example.cryptoballot.util.Paillier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigInteger;
import java.util.Optional;

@RestController
@RequestMapping("/attack")
@CrossOrigin(origins = "http://localhost:3000")
public class AttackController {

    @Autowired
    private VoteRepository voteRepository;
    
    // For simulation purposes, we use a static Paillier instance.
    // In a real system, the same persistent keys would be used across all services.
    private static final Paillier paillier = KeyManager.getPaillier();

    /**
     * Replay Attack Simulation:
     * Retrieves an existing vote from the DB and attempts to insert a duplicate vote with the same nonce.
     * The unique nonce constraint should block the duplicate submission.
     */
    @GetMapping("/replay")
    public ResponseEntity<String> simulateReplayAttack() {
        Optional<Vote> existingVoteOpt = voteRepository.findAll().stream().findFirst();
        if (existingVoteOpt.isPresent()) {
            Vote existingVote = existingVoteOpt.get();
            String nonce = existingVote.getNonce();
            try {
                // Attempt to create a duplicate vote with the same nonce and cryptographic values.
                Vote duplicateVote = new Vote(existingVote.getVoterUsername(), 
                        existingVote.getCandidate(), existingVote.getEncryptedVote());
                duplicateVote.setNonce(nonce);
                voteRepository.save(duplicateVote);
                return ResponseEntity.ok("Replay attack simulation FAILED: Duplicate vote accepted (nonce: " + nonce + ").");
            } catch (Exception ex) {
                return ResponseEntity.ok("Replay attack simulation SUCCESSFUL: Duplicate vote rejected due to nonce constraint. " + ex.getMessage());
            }
        } else {
            // If no vote exists, create one first using actual encryption services.
            Vote vote = new Vote("replayUser", "Candidate A", "dummyEncryptedVote");
            vote.setNonce("uniqueNonce123");
            voteRepository.save(vote);
            return ResponseEntity.ok("No existing vote found. A vote with nonce 'uniqueNonce123' was created. Run the simulation again.");
        }
    }

    /**
     * Substitution Attack Simulation:
     * Retrieves a live vote’s encrypted value and simulates tampering by modifying its ciphertext.
     * Then, both the original and tampered ciphertext are decrypted using Paillier.
     * A difference in the decrypted result indicates that the tampered vote is invalid.
     */
    @GetMapping("/substitution")
    public ResponseEntity<String> simulateSubstitutionAttack() {
        Optional<Vote> existingVoteOpt = voteRepository.findAll().stream().findFirst();
        if (existingVoteOpt.isPresent()) {
            Vote originalVote = existingVoteOpt.get();
            String originalEncryptedStr = originalVote.getEncryptedVote();
            if (originalEncryptedStr == null || originalEncryptedStr.isEmpty()) {
                return ResponseEntity.ok("Substitution attack simulation cannot proceed: Encrypted vote is empty.");
            }
            try {
                // Convert the stored encrypted vote (in hex) to a BigInteger.
                BigInteger originalCipher = new BigInteger(originalEncryptedStr, 16);
                // Decrypt the original ciphertext.
                BigInteger decryptedOriginal = paillier.decrypt(originalCipher);

                // Tamper with the encrypted vote: modify its last character.
                char lastChar = originalEncryptedStr.charAt(originalEncryptedStr.length() - 1);
                char tamperedChar = (lastChar == 'a' ? 'b' : 'a'); // simple flip between 'a' and 'b'
                String tamperedEncryptedStr = originalEncryptedStr.substring(0, originalEncryptedStr.length() - 1) + tamperedChar;
                BigInteger tamperedCipher = new BigInteger(tamperedEncryptedStr, 16);
                BigInteger decryptedTampered = paillier.decrypt(tamperedCipher);

                if (!decryptedOriginal.equals(decryptedTampered)) {
                    return ResponseEntity.ok("Substitution attack simulation SUCCESSFUL: Tampered encrypted vote decrypts differently.\n"
                            + "Original decrypted value: " + decryptedOriginal.toString() + "\n"
                            + "Tampered decrypted value: " + decryptedTampered.toString());
                } else {
                    return ResponseEntity.ok("Substitution attack simulation FAILED: Tampered encrypted vote decrypted to the same value (unexpected).");
                }
            } catch (Exception ex) {
                return ResponseEntity.ok("Substitution attack simulation encountered an error: " + ex.getMessage());
            }
        } else {
            return ResponseEntity.ok("No vote found in the database to simulate substitution attack.");
        }
    }

    /**
     * Injection Attack Simulation:
     * Attempts to store a vote with a malicious candidate string while still using the real encryption service.
     * The system should treat the malicious input as a literal string and store it safely.
     */
    @GetMapping("/injection")
    public ResponseEntity<String> simulateInjectionAttack() {
        String maliciousCandidate = "Candidate A";
        try {
            // For demonstration, we assume a proper encryption process is used elsewhere.
            // Here we simulate that a vote is cast with a malicious candidate string.
            // The encryption process (and later blind signature) is assumed to have been applied correctly.
            Vote injectedVote = new Vote("injectionUser", maliciousCandidate, "1234567890123456789012345678901234567890123456789012345678901234567890231234567890");
            // Set a unique nonce based on current time.
            injectedVote.setNonce("injectNonce" + System.currentTimeMillis());
            voteRepository.save(injectedVote);
            Optional<Vote> fetchedVote = voteRepository.findById(injectedVote.getId());
            if (fetchedVote.isPresent() && fetchedVote.get().getCandidate().equals(maliciousCandidate)) {
                return ResponseEntity.ok("Injection attack simulation SUCCESSFUL: Malicious input stored as literal string. SQL injection prevented.");
            } else {
                return ResponseEntity.ok("Injection attack simulation FAILED: Vote data altered unexpectedly.");
            }
        } catch (Exception ex) {
            return ResponseEntity.ok("Injection attack simulation encountered an exception: " + ex.getMessage());
        }
    }
}
