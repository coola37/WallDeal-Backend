package com.zerone.walldeal.api.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.zerone.walldeal.api.auth.FirebaseTokenAuthenticationFilter;
import com.zerone.walldeal.api.auth.FirebaseTokenVerifier;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void initializeFirebaseApp() {
        try {
            FileInputStream serviceAccount = new FileInputStream("./serviceAccountKey.json");
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            throw new RuntimeException("Error initializing Firebase App", e);
        }
    }

    @Bean
    public FirebaseTokenVerifier firebaseTokenVerifier() {
        return new FirebaseTokenVerifier();
    }

    @Bean
    public FirebaseTokenAuthenticationFilter firebaseTokenAuthenticationFilter() throws Exception {
        FirebaseTokenAuthenticationFilter filter = new FirebaseTokenAuthenticationFilter(firebaseTokenVerifier());
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        FirebaseTokenAuthenticationFilter filter = firebaseTokenAuthenticationFilter();
        filter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));

        http
                .csrf().disable() // Disable CSRF for simplicity, ensure to enable and configure properly in production
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/1.0/users/username-check/**").permitAll() // Allow access without authorization
                        .requestMatchers("/api/1.0/**").hasRole("ADMIN") // Require admin role for other /api/1.0/ endpoints
                        .anyRequest().permitAll() // Permit all other requests
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(new AccessDeniedHandlerImpl())
                );

        return http.build();
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        List<UserDetails> users = new ArrayList<>();
        UserDetails user = User.withUsername("user")
                .password(passwordEncoder().encode("password"))
                .roles("USER")
                .build();
        users.add(user);
        return new InMemoryUserDetailsManager(users);
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return new ProviderManager(Arrays.asList(authenticationProvider()));
    }
}