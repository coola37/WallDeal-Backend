package com.zerone.walldeal.api.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Entity
@Table(name = "wallpapers")
@NoArgsConstructor
public class Wallpaper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wallpaper_id", nullable = false, unique = true)
    private int wallpaperId;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "category")
    private String category;

    @Column(name = "like_count")
    private int likeCount;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "liked_users")
    private Set<String> likedUsers;

    @Column(name = "users_added_favorite")
    private Set<String> userAddedFavorite;

}