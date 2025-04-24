package com.example.cryptoballot.util;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

public class RsaUtil {
    // Generate an RSA key pair
    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        return keyGen.generateKeyPair();
    }

    // Blind a message: blinded = (m * (r^e mod n)) mod n
    public static BigInteger blindMessage(BigInteger m, BigInteger r, BigInteger e, BigInteger n) {
        return m.multiply(r.modPow(e, n)).mod(n);
    }

    // Unblind the signature: finalSignature = (blindSignature * r^-1 mod n) mod n
    public static BigInteger unblindSignature(BigInteger blindSignature, BigInteger r, BigInteger n) {
        BigInteger rInv = r.modInverse(n);
        return blindSignature.multiply(rInv).mod(n);
    }
}
