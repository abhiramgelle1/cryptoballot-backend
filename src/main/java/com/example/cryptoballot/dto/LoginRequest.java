package com.example.cryptoballot.dto;

public class LoginRequest {
    private String username;
    private String password; // The user's password

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
