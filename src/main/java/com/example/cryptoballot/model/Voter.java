package com.example.cryptoballot.model;

import jakarta.persistence.*;

@Entity
@Table(name = "voters")
public class Voter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    
    // The BCrypt hashed password is stored here.
    // (Ideally, rename this field to "passwordHash")
    @Column(columnDefinition = "TEXT")
    private String publicValue;

    public Voter() {}

    public Voter(String username, String publicValue) {
        this.username = username;
        this.publicValue = publicValue;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPublicValue() {
        return publicValue;
    }
    public void setPublicValue(String publicValue) {
        this.publicValue = publicValue;
    }
}
