package com.zerone.walldeal.api.service;

import com.zerone.walldeal.api.entity.User;
import com.zerone.walldeal.api.entity.Wallpaper;

import java.util.List;
import java.util.Set;

public interface WallpaperService {
    void createWallpaper(Wallpaper wallpaper);
    List<Wallpaper> wallpaperFindByUser(String userId);
    List<Wallpaper> getAllWallpapers();
    Wallpaper getWallpaper(int wallpaperId);
    List<Wallpaper> getWallpaperByCategory(String category);
    void addFavoritesWallpaper(String userId, int wallpaperId);
    List<Wallpaper> getWallpapersByFollowed(String currentUserId);
    void removeWallpaper(int wallpaperId);

    List<Wallpaper> getFavoriteWallpapers(String userId);
}
