package com.zerone.walldeal.api.exceptions;

public class WallpaperNotFoundException extends RuntimeException{
    public WallpaperNotFoundException(Integer id) {
        super("Wallpaper with ID " + id + " not found");
    }
}
