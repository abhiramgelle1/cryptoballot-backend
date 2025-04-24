package com.example.cryptoballot.model;

import jakarta.persistence.*;

@Entity
@Table(name = "votes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nonce"})
})
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The username of the voter who cast the vote
    private String voterUsername;

    // A plain-text candidate name (e.g., "Candidate A", "Candidate B", etc.)
    private String candidate;

    // The encrypted numeric portion of the vote (in hex), stored as TEXT to prevent formatting issues
    @Column(columnDefinition = "TEXT")
    private String encryptedVote;
    
    // Nonce field to prevent replay attacks.
    @Column(unique = true)
    private String nonce;

    public Vote() {}

    public Vote(String voterUsername, String candidate, String encryptedVote) {
        this.voterUsername = voterUsername;
        this.candidate = candidate;
        this.encryptedVote = encryptedVote;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getVoterUsername() { return voterUsername; }
    public void setVoterUsername(String voterUsername) { this.voterUsername = voterUsername; }

    public String getCandidate() { return candidate; }
    public void setCandidate(String candidate) { this.candidate = candidate; }

    public String getEncryptedVote() { return encryptedVote; }
    public void setEncryptedVote(String encryptedVote) { this.encryptedVote = encryptedVote; }
    
    public String getNonce() { return nonce; }
    public void setNonce(String nonce) { this.nonce = nonce; }
}
