package com.zerone.walldeal.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "couple_id")
    private String coupleId;

    @Column(name = "profile_photo")
    private String profilePhoto;

    @Column(name = "fcm_token")
    private String fcmToken;

    @Column(name = "favorite_wallpapers")
    private Set<Integer> favoriteWallpapers;

    @Column(name = "added_favorites")
    private Set<Integer> addedFavorites;

    @Column(name = "followers")
    private Set<String> followers;

    @Column(name = "followed")
    private Set<String> followed;

}