package com.zerone.walldeal.api.auth;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;

public class FirebaseTokenAuthentication extends AbstractAuthenticationToken {
    private final String token;

    public FirebaseTokenAuthentication(String token) {
        super(Collections.emptyList());
        this.token = token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }
}