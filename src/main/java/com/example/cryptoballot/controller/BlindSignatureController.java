package com.example.cryptoballot.controller;

import com.example.cryptoballot.util.RsaUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@RestController
@RequestMapping("/crypto/rsa/blind")
@CrossOrigin(origins = "http://localhost:3000")
public class BlindSignatureController {
    private final KeyPair keyPair;

    public BlindSignatureController() throws Exception {
        keyPair = RsaUtil.generateKeyPair();
    }

    @GetMapping("/params")
    public ResponseEntity<String> getParameters() {
        RSAPublicKey pub = (RSAPublicKey) keyPair.getPublic();
        BigInteger n = pub.getModulus();
        BigInteger e = pub.getPublicExponent();
        String response = "n: " + n.toString() + "\ne: " + e.toString();
        return ResponseEntity.ok(response);
    }
    
    
    @PostMapping("/fullBlind")
    public ResponseEntity<String> fullBlindSignature(
            @RequestParam String message,
            @RequestParam String rStr
    ) {
        try {
            // Parse the encrypted vote as hexadecimal
            BigInteger m = new BigInteger(message, 16);
            BigInteger r = new BigInteger(rStr);  // r is provided in decimal

            RSAPublicKey pub = (RSAPublicKey) keyPair.getPublic();
            BigInteger n = pub.getModulus();
            BigInteger e = pub.getPublicExponent();

            BigInteger blindedMessage = RsaUtil.blindMessage(m, r, e, n);
            RSAPrivateKey priv = (RSAPrivateKey) keyPair.getPrivate();
            BigInteger blindSignature = blindedMessage.modPow(priv.getPrivateExponent(), n);
            BigInteger finalSignature = RsaUtil.unblindSignature(blindSignature, r, n);
            
            String result = "Original Message: " + m.toString() + "\n" +
                    "Blinding Factor: " + r.toString() + "\n" +
                    "Blinded Message: " + blindedMessage.toString() + "\n" +
                    "Blind Signature (s'): " + blindSignature.toString() + "\n" +
                    "Final Signature: " + finalSignature.toString();
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Error: " + ex.getMessage());
        }
    }


//    @PostMapping("/fullBlind")
//    public ResponseEntity<String> fullBlindSignature(
//            @RequestParam String message,
//            @RequestParam String rStr
//    ) {
//        try {
//            BigInteger m = new BigInteger(message);
//            BigInteger r = new BigInteger(rStr);
//            RSAPublicKey pub = (RSAPublicKey) keyPair.getPublic();
//            BigInteger n = pub.getModulus();
//            BigInteger e = pub.getPublicExponent();
//            BigInteger blindedMessage = RsaUtil.blindMessage(m, r, e, n);
//            RSAPrivateKey priv = (RSAPrivateKey) keyPair.getPrivate();
//            BigInteger blindSignature = blindedMessage.modPow(priv.getPrivateExponent(), n);
//            BigInteger finalSignature = RsaUtil.unblindSignature(blindSignature, r, n);
//            String result = "Original Message: " + m.toString() + "\n" +
//                    "Blinding Factor: " + r.toString() + "\n" +
//                    "Blinded Message: " + blindedMessage.toString() + "\n" +
//                    "Blind Signature (s'): " + blindSignature.toString() + "\n" +
//                    "Final Signature: " + finalSignature.toString();
//            return ResponseEntity.ok(result);
//        } catch (Exception ex) {
//            return ResponseEntity.badRequest().body("Error: " + ex.getMessage());
//        }
//    }
}
