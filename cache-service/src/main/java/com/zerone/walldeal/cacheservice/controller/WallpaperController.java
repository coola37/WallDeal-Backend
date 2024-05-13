package com.zerone.walldeal.cacheservice.controller;

import com.google.protobuf.Api;
import com.zerone.walldeal.cacheservice.entity.ApiResponse;
import com.zerone.walldeal.cacheservice.entity.Wallpaper;
import com.zerone.walldeal.cacheservice.service.WallpaperService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/1.0/wallpaper-cache")
@RequiredArgsConstructor
public class WallpaperController {
    private final WallpaperService wallpaperService;

    @GetMapping
    public ResponseEntity<ApiResponse> getWallpapers(){
        List<Wallpaper> wallpapers = wallpaperService.getWallpapers();
        ApiResponse response = ApiResponse.builder()
                .response("Succes")
                .payload(wallpapers)
                .build();
        return ResponseEntity.ok(response);
    }
}
