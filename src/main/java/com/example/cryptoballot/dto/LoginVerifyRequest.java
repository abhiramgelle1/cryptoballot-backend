package com.example.cryptoballot.dto;

public class LoginVerifyRequest {
    private String username;
    private String response;

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getResponse() {
        return response;
    }
    public void setResponse(String response) {
        this.response = response;
    }
}
