package com.example.cryptoballot.dto;

public class RegistrationRequest {
    private String username;
    private String password; // Now using "password" instead of "secret"

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
