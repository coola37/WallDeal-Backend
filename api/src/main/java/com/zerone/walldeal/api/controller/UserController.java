package com.zerone.walldeal.api.controller;

import com.zerone.walldeal.api.auth.FirebaseTokenVerifier;
import com.zerone.walldeal.api.entity.User;
import com.zerone.walldeal.api.entity.Wallpaper;
import com.zerone.walldeal.api.service.CacheService;
import com.zerone.walldeal.api.service.UserService;
import com.zerone.walldeal.api.service.WallpaperService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/1.0/users")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    WallpaperService wallpaperService;

    @Autowired
    FirebaseTokenVerifier firebaseTokenVerifier;

    @Autowired
    CacheService cacheService;

    @PostMapping
    ResponseEntity<User> saveUser(@RequestBody User user){
        User newUser = copyUser(user);

        if(user.getFollowed() != null && !(user.getFollowed().isEmpty())){
            newUser.setFollowed(user.getFollowed());
        }else{
            newUser.setFollowed(Collections.emptySet());
        }
        if(user.getFollowers() != null && !(user.getFollowers().isEmpty())){
            newUser.setFollowers(user.getFollowers());
        }else{
            newUser.setFollowers(Collections.emptySet());
        }
        if(user.getFavoriteWallpapers() != null && !(user.getFavoriteWallpapers().isEmpty())){
            newUser.setFavoriteWallpapers(user.getFavoriteWallpapers());
        }else{
            newUser.setFavoriteWallpapers(Collections.emptySet());
        }
        if(user.getProfilePhoto() != null && !(user.getProfilePhoto().isEmpty())){
            newUser.setProfilePhoto(user.getProfilePhoto());
        }else{
            newUser.setProfilePhoto("https://firebasestorage.googleapis.com/v0/b/wall-deal.appspot.com/o/logo%2Fuser.png?alt=media&token=f38aa1f6-a721-4a83-ace9-6123c30d57a6");
        }
        userService.createUser(newUser);
        cacheService.delete("users");
        return ResponseEntity.ok(newUser);
    }

    @GetMapping("/{userId}")
    ResponseEntity<User> getUserByID(@RequestHeader("Authorization") String firebaseToken,@PathVariable String userId){
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @GetMapping
    ResponseEntity<Set<User>> getAllUsers(@RequestHeader("Authorization") String firebaseToken){
        Set<User> users = (Set<User>) cacheService.get("users");
        if(users == null){
            users = userService.getAllUsers();
            cacheService.save("users", users, 10, TimeUnit.MINUTES);
            return ResponseEntity.ok(users);
        }else {
            return ResponseEntity.ok(users);
        }
    }
    @GetMapping("/check/favorites/{userId}/{wallpaperId}")
    ResponseEntity<Boolean> checkFavorites(@PathVariable String userId, @PathVariable Integer wallpaperId){
        User user = userService.getUserById(userId);
        Set<Integer> favorites = user.getFavoriteWallpapers();
        if(favorites.contains(wallpaperId)) return ResponseEntity.ok(true);
        else return ResponseEntity.ok(false);
    }
    @PutMapping("/follow-or-unfollow/{currentUserId}/{targetUserId}")
    ResponseEntity followOrUnfollow(@PathVariable String currentUserId, @PathVariable String targetUserId){
        try{
            userService.followOrUnfollow(currentUserId, targetUserId);
            return new ResponseEntity<>(OK);
        }catch (RuntimeException ex){
            throw ex;
        }
    }

    @GetMapping("/check-follow/{currentUserId}/{targetUserId}")
    ResponseEntity<Boolean> checkFollow(@PathVariable String currentUserId, @PathVariable String targetUserId) {
        try{
            return ResponseEntity.ok(userService.checkFollow(currentUserId, targetUserId));
        }catch (RuntimeException ex){
            throw ex;
        }
    }
    @PutMapping("/edit-profile")
    ResponseEntity editProfile(@RequestBody User user){
        try {
            userService.editUser(user);
            return new ResponseEntity<>(OK);
        }catch (RuntimeException e){
            throw e;
        }
    }

    @DeleteMapping("/delete-user/{userId}")
    ResponseEntity deleteUser(@PathVariable String userId){
        try {
            userService.deleteAccount(userId);
            cacheService.delete("users");
            return new ResponseEntity(OK);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/check-user/{userId}")
    ResponseEntity<Boolean> checkUser(@PathVariable String userId)  {
        User user = userService.getUserById(userId);
        if(user != null){
            return ResponseEntity.ok(true);
        }else{
            return ResponseEntity.ok(false);
        }
    }

    User copyUser(User user){
        User newUser = new User();
        newUser.setUserId(user.getUserId());
        newUser.setEmail(user.getEmail());
        newUser.setUsername(user.getUsername());
        newUser.setCoupleId(user.getCoupleId());
        newUser.setProfilePhoto(user.getProfilePhoto());
        newUser.setFcmToken(user.getFcmToken());
        newUser.setFavoriteWallpapers(user.getFavoriteWallpapers());
        newUser.setAddedFavorites(user.getAddedFavorites());
        newUser.setFollowers(user.getFollowers());
        newUser.setFollowed(user.getFollowed());
        return newUser;
    }

    @GetMapping("/username-check/{username}")
    ResponseEntity<Boolean> checkUsingUsername(@PathVariable String username){
        Set<User> users = userService.getAllUsers();
        boolean isUsing = false;
        for (User user : users){
            if(user.getUsername().equals(username)){
                isUsing = true;
                break;
            }
        }
        return ResponseEntity.ok(isUsing);
    }
}
