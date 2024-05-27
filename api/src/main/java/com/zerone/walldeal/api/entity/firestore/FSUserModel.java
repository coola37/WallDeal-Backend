package com.zerone.walldeal.api.entity.firestore;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FSUserModel {
    private String userId;

    private String email;

    private String username;

    private String coupleId;

    private String profilePhoto;

    private String fcmToken;

    private List<Integer> favoriteWallpapers;

    private List<Integer> addedFavorites;

    private List<String> followers;

    private List<String> followed;
}
