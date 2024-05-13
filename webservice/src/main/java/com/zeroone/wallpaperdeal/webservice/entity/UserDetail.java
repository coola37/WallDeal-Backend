package com.zeroone.wallpaperdeal.webservice.entity;

import com.google.cloud.firestore.annotation.Exclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetail {
    String profilePhoto;
    List<Wallpaper> favoriteWallpapers;
    @Exclude
    transient List<UserDTO> followers;
    transient List<UserDTO> followed;
}
