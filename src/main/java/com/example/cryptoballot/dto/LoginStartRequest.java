package com.example.cryptoballot.dto;

public class LoginStartRequest {
    private String username;
    private String commitment;

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getCommitment() {
        return commitment;
    }
    public void setCommitment(String commitment) {
        this.commitment = commitment;
    }
}
