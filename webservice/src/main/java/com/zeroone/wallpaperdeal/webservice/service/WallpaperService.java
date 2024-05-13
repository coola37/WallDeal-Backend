package com.zeroone.wallpaperdeal.webservice.service;

import com.zeroone.wallpaperdeal.webservice.entity.Wallpaper;

import java.util.List;

public interface WallpaperService {
    public Wallpaper createWallpaper(Wallpaper wallpaper);
    public Wallpaper getWallpaper(String wallpaperId);
    public List<Wallpaper> getWallpapers();
    public List<Wallpaper> getWallpaperByOwner(String owner);
    public List<Wallpaper> getWallpapersByCategory(String categoryName);
    public void addFavoritesWallpaper(String userId, String wallpaperId);
    public List<Wallpaper> getWallpapersByFollowed(String currentUserId);
    public void removeWallpaper(String wallpaperId);
}
