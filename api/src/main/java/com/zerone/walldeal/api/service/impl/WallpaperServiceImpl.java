package com.zerone.walldeal.api.service.impl;

import com.zerone.walldeal.api.entity.User;
import com.zerone.walldeal.api.entity.Wallpaper;
import com.zerone.walldeal.api.exceptions.WallpaperNotFoundException;
import com.zerone.walldeal.api.repository.UserRepository;
import com.zerone.walldeal.api.repository.WallpaperRepository;
import com.zerone.walldeal.api.service.UserService;
import com.zerone.walldeal.api.service.WallpaperService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
@RequiredArgsConstructor
public class WallpaperServiceImpl implements WallpaperService {

    @Autowired
    WallpaperRepository wallpaperRepository;
    @Autowired
    @Lazy
    UserService userService;
    @Override
    public void createWallpaper(Wallpaper wallpaper) {
        wallpaperRepository.save(wallpaper);
    }

    @Override
    public List<Wallpaper> wallpaperFindByUser(String userId) {
        return wallpaperRepository.findByUser_UserId(userId);
    }

    @Override
    public List<Wallpaper> getAllWallpapers() {
        return wallpaperRepository.findAll();
    }

    @Override
    public Wallpaper getWallpaper(int wallpaperId) {
        return wallpaperRepository
                .findById(wallpaperId).orElse(new Wallpaper());
    }

    @Override
    public List<Wallpaper> getWallpaperByCategory(String category) {
        return wallpaperRepository.findByCategory(category);
    }

    @Override
    public void addFavoritesWallpaper(String userId, int wallpaperId) {
        User user = userService.getUserById(userId);
        Wallpaper wallpaper = getWallpaper(wallpaperId);

        // Favori listeleri oluşturuluyor
        Set<Integer> favoriteList = new HashSet<>();
        Set<Integer> addedFavorites = new HashSet<>();

        if(user.getFavoriteWallpapers() != null){
            favoriteList = user.getFavoriteWallpapers();
        }

        if(user.getAddedFavorites() != null){
            addedFavorites = user.getAddedFavorites();
        }

        if(favoriteList.contains(wallpaperId)){
            favoriteList.remove(wallpaperId);
            if(addedFavorites.contains(wallpaperId)) {
                addedFavorites.remove(wallpaperId);
            } else {
                addedFavorites.add(wallpaperId);
            }
        } else {
            favoriteList.add(wallpaperId);
            if(addedFavorites.contains(wallpaperId)) {
                addedFavorites.remove(wallpaperId);
            } else {
                addedFavorites.add(wallpaperId);
            }
        }

        user.setFavoriteWallpapers(favoriteList);
        user.setAddedFavorites(addedFavorites);
        userService.createUser(user);

        Set<String> userAddedFavorite = new HashSet<>();
        if(wallpaper.getUserAddedFavorite() != null) {
            userAddedFavorite = wallpaper.getUserAddedFavorite();
            if(userAddedFavorite.contains(userId)) {
                userAddedFavorite.remove(userId);
            } else {
                userAddedFavorite.add(userId);
            }
        } else {
            userAddedFavorite.add(userId);
        }

        System.err.println(userAddedFavorite);
        wallpaper.setUserAddedFavorite(userAddedFavorite);
        createWallpaper(wallpaper);
    }


    @Override
    public List<Wallpaper> getWallpapersByFollowed(String currentUserId) {
        User currentUser = userService.getUserById(currentUserId);
        List<Wallpaper> wallpapersByFollowed = new ArrayList<>();
        List<Wallpaper> wallpapersSortLike = new ArrayList<>();
        List<String> userFollowedList = currentUser.getFollowed().stream().toList();
        try {
            if(userFollowedList.isEmpty() || userFollowedList == null) {
                wallpapersSortLike = getAllWallpapers();
                wallpapersSortLike = wallpapersSortLike.stream()
                        .sorted(Comparator.comparing(Wallpaper::getLikeCount).reversed())
                        .limit(50)
                        .toList();
                return wallpapersSortLike;
            }else{
                for(String userId : userFollowedList){
                    List<Wallpaper> wallpapersByUser = wallpaperFindByUser(userId);
                    wallpapersByFollowed.addAll(wallpapersByUser);
                }
                wallpapersSortLike = getAllWallpapers();
                wallpapersSortLike = wallpapersSortLike.stream()
                        .sorted(Comparator.comparing(Wallpaper::getLikeCount).reversed())
                        .limit(50)
                        .toList();
                for(Wallpaper wallpaper : wallpapersSortLike){
                    if(!wallpapersByFollowed.contains(wallpaper)){
                        wallpapersByFollowed.add(wallpaper);
                    }
                }
                return wallpapersByFollowed;
            }
        }catch (RuntimeException e){
            throw e;
        }
    }

    @Override
    public void removeWallpaper(int wallpaperId) {
        Wallpaper targetWallpaper = getWallpaper(wallpaperId);
        Set<String> users = targetWallpaper.getUserAddedFavorite();
        try {
            for(String userId: users){
                User user = userService.getUserById(userId);
                Set<Integer> favoriteList = user.getFavoriteWallpapers();
                favoriteList.remove(wallpaperId);
                user.setFavoriteWallpapers(favoriteList);
                userService.createUser(user);
            }

            wallpaperRepository.delete(targetWallpaper);
        }catch (RuntimeException exception){
            throw exception;
        }
    }

    @Override
    public List<Wallpaper> getFavoriteWallpapers(String userId) {
        User currentUser =userService.getUserById(userId);
        Set<Integer> favoriteList = currentUser.getFavoriteWallpapers();
        List<Wallpaper> favorites = new ArrayList<>();
        for(int id : favoriteList){
            Wallpaper wallpaper = getWallpaper(id);
            favorites.add(wallpaper);
        }
        return favorites;
    }
}
