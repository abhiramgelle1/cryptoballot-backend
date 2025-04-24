package com.example.cryptoballot.dto;

public class LoginChallengeResponse {
    private int challenge;
    public LoginChallengeResponse(int challenge) { this.challenge = challenge; }
    public int getChallenge() { return challenge; }
    public void setChallenge(int challenge) { this.challenge = challenge; }
}
