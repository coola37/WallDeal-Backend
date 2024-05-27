package com.zerone.walldeal.api.auth;

import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


import com.google.firebase.auth.FirebaseToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;

public class FirebaseTokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final FirebaseTokenVerifier firebaseTokenVerifier;

    public FirebaseTokenAuthenticationFilter(FirebaseTokenVerifier firebaseTokenVerifier) {
        super(new RequestHeaderRequestMatcher("Authorization"));
        this.firebaseTokenVerifier = firebaseTokenVerifier;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    private Authentication getAuthentication(String jwtToken) {
        FirebaseToken token = firebaseTokenVerifier.verifyToken(jwtToken);
        if (token != null) {
            List<GrantedAuthority> authoritiesList = new ArrayList<>();
            authoritiesList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            System.err.println("In authentication verify token, assigned ROLE_ADMIN");
            return new UsernamePasswordAuthenticationToken(token.getUid(), token, authoritiesList);
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        System.err.println("Successful Authentication: " + authResult.getAuthorities());
        chain.doFilter(request, response);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // "Bearer " ifadesini kaldır
            if (firebaseTokenVerifier.verifyToken(token) != null) {
                Authentication auth = getAuthentication(token);
                if (auth != null && auth.isAuthenticated()) {
                    System.err.println("Authentication successful: " + auth.getAuthorities());
                    return auth;
                }
            }
        }
        // Token doğrulanamadı veya eksik
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid or missing token");
        return null;
    }
}