package com.example.cryptoballot.util;

import java.math.BigInteger;
import java.security.SecureRandom;

public class Paillier {
    public BigInteger p, q, n, nsquare, g, lambda;
    private int bitLength;
    private SecureRandom random;

    public Paillier(int bitLengthVal, int certainty) {
        bitLength = bitLengthVal;
        random = new SecureRandom();
        p = new BigInteger(bitLength / 2, certainty, random);
        q = new BigInteger(bitLength / 2, certainty, random);
        n = p.multiply(q);
        nsquare = n.multiply(n);
        lambda = p.subtract(BigInteger.ONE)
                   .multiply(q.subtract(BigInteger.ONE))
                   .divide(p.subtract(BigInteger.ONE).gcd(q.subtract(BigInteger.ONE)));
        g = n.add(BigInteger.ONE);
    }

    public Paillier() {
        this(512, 64);
    }

    // Encryption: E(m) = g^m * r^n mod nsquare
    public BigInteger encrypt(BigInteger m) {
        BigInteger r = new BigInteger(bitLength, random);
        while (r.compareTo(n) >= 0) {
            r = new BigInteger(bitLength, random);
        }
        return g.modPow(m, nsquare).multiply(r.modPow(n, nsquare)).mod(nsquare);
    }

    // Decryption: m = L(c^lambda mod nsquare) / L(g^lambda mod nsquare)
    // where L(u) = (u - 1) / n.
    public BigInteger decrypt(BigInteger c) {
        BigInteger u = c.modPow(lambda, nsquare).subtract(BigInteger.ONE).divide(n);
        BigInteger L = g.modPow(lambda, nsquare).subtract(BigInteger.ONE).divide(n);
        return u.multiply(L.modInverse(n)).mod(n);
    }
}
