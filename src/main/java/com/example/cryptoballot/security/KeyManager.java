package com.example.cryptoballot.security;

import com.example.cryptoballot.util.Paillier;

public class KeyManager {
    private static Paillier paillierInstance;

    public static Paillier getPaillier() {
        return paillierInstance;
    }

    public static void setPaillier(Paillier p) {
        paillierInstance = p;
    }
}
