package com.zerone.walldeal.api.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

public class FirebaseTokenVerifier {
    private final FirebaseAuth firebaseAuth;

    public FirebaseTokenVerifier() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public FirebaseToken verifyToken(String token) {
        try {
            FirebaseToken decodedToken = firebaseAuth.verifyIdToken(token);
            System.err.println("Verify");
            return decodedToken;
        } catch (FirebaseAuthException e) {
            System.err.println("No Verify");
            return null;
        }
    }
}
