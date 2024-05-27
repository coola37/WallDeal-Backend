package com.zerone.walldeal.api.controller;

import com.zerone.walldeal.api.entity.User;
import com.zerone.walldeal.api.entity.Wallpaper;
import com.zerone.walldeal.api.request.LikeRequest;
import com.zerone.walldeal.api.response.ApiResponse;
import com.zerone.walldeal.api.service.CacheService;
import com.zerone.walldeal.api.service.UserService;
import com.zerone.walldeal.api.service.WallpaperService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@RequestMapping("/api/1.0/wallpapers")
@RequestScope
public class WallpaperController {

    @Autowired
    WallpaperService wallpaperService;
    @Autowired
    @Lazy
    UserService userService;

    @Autowired
    CacheService cacheService;

    @PostMapping
    void saveWallpaper(@RequestHeader("Authorization") String firebaseToken, @RequestBody Wallpaper wallpaper){
        Wallpaper response = new Wallpaper();
        response.setUser(wallpaper.getUser());
        response.setDescription(wallpaper.getDescription());
        response.setImageUrl(wallpaper.getImageUrl());
        response.setCategory(wallpaper.getCategory());
        response.setLikedUsers(wallpaper.getLikedUsers());
        response.setLikeCount(0);
        response.setUserAddedFavorite(new HashSet<>());
        wallpaperService.createWallpaper(response);
        cacheService.delete("wallpapers");
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getWallpapers() {
        List<Wallpaper> wallpapers = (List<Wallpaper>) cacheService.get("wallpapers");
        if (wallpapers == null) {
            wallpapers = wallpaperService.getAllWallpapers();
            cacheService.save("wallpapers", wallpapers, 10, TimeUnit.MINUTES);
        }
        ApiResponse response = ApiResponse.builder()
                .response("Success")
                .payload(wallpapers)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{category}")
    ResponseEntity<List<Wallpaper>> getWallpaperByCategory(@PathVariable String category) {
        List<Wallpaper> wallpapers = (List<Wallpaper>) cacheService.get("wallpapers");
        if (wallpapers == null) {
            List<Wallpaper> allWallpapers = wallpaperService.getAllWallpapers();
            cacheService.save("wallpapers", allWallpapers, 10, TimeUnit.MINUTES);
            wallpapers = wallpaperService.getWallpaperByCategory(category);
            return ResponseEntity.ok(wallpapers);
        } else {
            List<Wallpaper> filteredWallpapers = wallpapers.stream()
                    .filter(wallpaper -> category.equals(wallpaper.getCategory()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(filteredWallpapers);
        }
    }


    @GetMapping("/get/{wallpaperId}")
    ResponseEntity<Wallpaper> getWallpaperById(@PathVariable int wallpaperId){
        Wallpaper wallpaper = wallpaperService.getWallpaper(wallpaperId);
        return ResponseEntity.ok(wallpaper);
    }

    @GetMapping("/owner/{id}")
    ResponseEntity<ApiResponse> getWallpaperByOwner(@PathVariable String id){
        List<Wallpaper> wallpapers = (List<Wallpaper>) cacheService.get("wallpapers");
        if(wallpapers == null){
            List<Wallpaper> allWallpapers = wallpaperService.getAllWallpapers();
            cacheService.save("wallpapers", allWallpapers, 10, TimeUnit.MINUTES);
            ApiResponse response = ApiResponse.builder()
                    .response("Succes")
                    .payload(wallpaperService.wallpaperFindByUser(id))
                    .build();
            return ResponseEntity.ok(response);
        }else{
            List<Wallpaper> filteredWallpapers = wallpapers.stream()
                    .filter(wallpaper -> id.equals(wallpaper.getUser().getUserId()))
                    .collect(Collectors.toList());
            ApiResponse response = ApiResponse.builder()
                    .response("Succes")
                    .payload(filteredWallpapers)
                    .build();
            return ResponseEntity.ok(response);
        }
    }

    @PutMapping("/like/{wallpaperId}")
    void likeOrDislike(@PathVariable int wallpaperId, @RequestBody LikeRequest likeRequest){
        Wallpaper wallpaper = wallpaperService.getWallpaper(wallpaperId);
        if(wallpaper.getLikedUsers() != null){
            if(wallpaper.getLikedUsers().contains(likeRequest.getCurrentUserId())){
                Set<String> likedList = wallpaper.getLikedUsers();
                likedList.remove(likeRequest.getCurrentUserId());
                wallpaper.setLikedUsers(likedList);
                wallpaper.setLikeCount(wallpaper.getLikeCount() - 1);
                wallpaperService.createWallpaper(wallpaper);

            }  else {
                Set<String> likedList = wallpaper.getLikedUsers();
                likedList.add(likeRequest.getCurrentUserId());
                wallpaper.setLikedUsers(likedList);
                wallpaper.setLikeCount(wallpaper.getLikeCount() + 1);
                wallpaperService.createWallpaper(wallpaper);

            }
        }else{
            Set<String> likedList = Collections.emptySet();
            likedList.add(likeRequest.getCurrentUserId());
            wallpaper.setLikedUsers(likedList);
            wallpaper.setLikeCount(wallpaper.getLikeCount() + 1);
            wallpaperService.createWallpaper(wallpaper);
        }
    }

    @GetMapping("/like/control/{wallpaperId}/{currentUserId}")
    ResponseEntity<Boolean> checkLike(@PathVariable int wallpaperId, @PathVariable String currentUserId){
        Wallpaper wallpaper = wallpaperService.getWallpaper(wallpaperId);
        if(wallpaper.getLikedUsers() != null){
            if(wallpaper.getLikedUsers().contains(currentUserId)){
                return ResponseEntity.ok(true);
            }  else {
                return ResponseEntity.ok(false);
            }
        }else{
            return ResponseEntity.ok(false);
        }
    }

    @PutMapping("/favorite/{userId}/{wallpaperId}")
    void addFavorite(@PathVariable String userId, @PathVariable int wallpaperId){
        wallpaperService.addFavoritesWallpaper(userId, wallpaperId);
    }

    @GetMapping("/get-wallpapers-by-followed/{currentUserId}")
    ResponseEntity<ApiResponse> getWallpaperByFollowed(@PathVariable String currentUserId){
        try{
            ApiResponse apiResponse = ApiResponse.builder()
                    .response("Succes")
                    .payload(wallpaperService.getWallpapersByFollowed(currentUserId))
                    .build();
            return ResponseEntity.ok(apiResponse);
        }catch (RuntimeException ex){
            throw ex;
        }
    }

    @DeleteMapping("/delete/{wallpaperId}")
    ResponseEntity removeWallpaper(@PathVariable int wallpaperId){
        try{
            wallpaperService.removeWallpaper(wallpaperId);
            cacheService.delete("wallpapers");
            return new ResponseEntity(OK);
        }catch (RuntimeException ex){
            throw ex;
        }
    }

    @GetMapping("/get-favorite-wallpaper/{userId}")
    ResponseEntity<List<Wallpaper>> getFavorites(@PathVariable String userId) {
        List<Wallpaper> wallpapers = (List<Wallpaper>) cacheService.get("wallpapers");
        if (wallpapers == null) {
            List<Wallpaper> allWallpapers = wallpaperService.getAllWallpapers();
            cacheService.save("wallpapers", allWallpapers, 10, TimeUnit.MINUTES);
            return ResponseEntity.ok(wallpaperService.getFavoriteWallpapers(userId));
        } else {
            List<Wallpaper> filteredWallpapers = wallpapers.stream()
                    .filter(wallpaper -> wallpaper.getUserAddedFavorite().contains(userId))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(filteredWallpapers);
        }
    }
}