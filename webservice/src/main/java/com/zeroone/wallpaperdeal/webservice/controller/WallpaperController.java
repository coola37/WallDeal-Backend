package com.zeroone.wallpaperdeal.webservice.controller;

import com.google.protobuf.Api;
import com.zeroone.wallpaperdeal.webservice.entity.Wallpaper;
import com.zeroone.wallpaperdeal.webservice.request.LikeRequest;
import com.zeroone.wallpaperdeal.webservice.response.ApiResponse;
import com.zeroone.wallpaperdeal.webservice.service.WallpaperService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/1.0/wallpapers")
public class WallpaperController {
    @Autowired
    WallpaperService wallpaperService;

    @PostMapping
    void saveWallpaper(@RequestBody Wallpaper wallpaper){
        Wallpaper response = Wallpaper.builder()
                .wallpaperId(wallpaper.getWallpaperId())
                .owner(wallpaper.getOwner())
                .description(wallpaper.getDescription())
                .imageUrl(wallpaper.getImageUrl())
                .category(wallpaper.getCategory())
                .likedUser(wallpaper.getLikedUser())
                .likeCount(0)
                .build();
        wallpaperService.createWallpaper(wallpaper);
    }

    @GetMapping
    ResponseEntity<ApiResponse> getWallpapers(){
        ApiResponse response = ApiResponse.builder()
                .response("Succes")
                .payload(wallpaperService.getWallpapers())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{category}")
    ResponseEntity<List<Wallpaper>> getWallpaperByCategory(@PathVariable String category){
        return ResponseEntity.ok(wallpaperService.getWallpapersByCategory(category));
    }

    @GetMapping("/get/{wallpaperId}")
    ResponseEntity<Wallpaper> getWallpaperById(@PathVariable String wallpaperId){
        Wallpaper wallpaper = wallpaperService.getWallpaper(wallpaperId);
        return ResponseEntity.ok(wallpaper);
    }

    @GetMapping("/owner/{id}")
    ResponseEntity<ApiResponse> getWallpaperByOwner(@PathVariable String id){
        ApiResponse response = ApiResponse.builder()
                .response("Succes")
                .payload(wallpaperService.getWallpaperByOwner(id))
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/like/{wallpaperId}")
    void likeOrDislike(@PathVariable String wallpaperId, @RequestBody LikeRequest likeRequest){
        Wallpaper wallpaper = wallpaperService.getWallpaper(wallpaperId);
        if(wallpaper.getLikedUser() != null){
            if(wallpaper.getLikedUser().contains(likeRequest.getCurrentUserId())){
                List<String> likedList = wallpaper.getLikedUser();
                likedList.remove(likeRequest.getCurrentUserId());
                wallpaper.setLikedUser(likedList);
                wallpaper.setLikeCount(wallpaper.getLikeCount() - 1);
                wallpaperService.createWallpaper(wallpaper);

            }  else {
                List<String> likedList = wallpaper.getLikedUser();
                likedList.add(likeRequest.getCurrentUserId());
                wallpaper.setLikedUser(likedList);
                wallpaper.setLikeCount(wallpaper.getLikeCount() + 1);
                wallpaperService.createWallpaper(wallpaper);

            }
        }else{
            List<String> likedList = new ArrayList<>();
            likedList.add(likeRequest.getCurrentUserId());
            wallpaper.setLikedUser(likedList);
            wallpaper.setLikeCount(wallpaper.getLikeCount() + 1);
            wallpaperService.createWallpaper(wallpaper);

        }
    }

    @GetMapping("/like/control/{wallpaperId}/{currentUserId}")
    ResponseEntity<Boolean> checkLike(@PathVariable String wallpaperId, @PathVariable String currentUserId){
        Wallpaper wallpaper = wallpaperService.getWallpaper(wallpaperId);
        if(wallpaper.getLikedUser() != null){
            if(wallpaper.getLikedUser().contains(currentUserId)){
                return ResponseEntity.ok(true);
            }  else {
                return ResponseEntity.ok(false);
            }
        }else{
            return ResponseEntity.ok(false);
        }
    }

    @PutMapping("/favorite/{userId}/{wallpaperId}")
    void addFavorite(@PathVariable String userId, @PathVariable String wallpaperId){
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
    ResponseEntity removeWallpaper(@PathVariable String wallpaperId){
        try{
            wallpaperService.removeWallpaper(wallpaperId);
            return new ResponseEntity(OK);
        }catch (RuntimeException ex){
            throw ex;
        }
    }
}
