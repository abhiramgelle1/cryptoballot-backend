package com.example.cryptoballot.util;

import java.math.BigInteger;

public class ZkpUtil {
    // A 256-bit prime modulus (example safe prime in hexadecimal)
    public static final BigInteger MODULUS_N = new BigInteger("F7E75FDC469067FFDC4E847C51F452DF", 16);

    // Computes base^exponent mod modulus using BigInteger arithmetic.
    public static BigInteger modExp(BigInteger base, BigInteger exponent, BigInteger modulus) {
        return base.modPow(exponent, modulus);
    }

    // Compute public value = secret^2 mod MODULUS_N
    public static BigInteger generatePublicValue(BigInteger secret) {
        return modExp(secret, BigInteger.valueOf(2), MODULUS_N);
    }
}
