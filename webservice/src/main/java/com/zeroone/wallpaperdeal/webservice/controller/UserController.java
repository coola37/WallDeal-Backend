package com.zeroone.wallpaperdeal.webservice.controller;

import com.zeroone.wallpaperdeal.webservice.entity.User;
import com.zeroone.wallpaperdeal.webservice.entity.UserDetail;
import com.zeroone.wallpaperdeal.webservice.entity.Wallpaper;
import com.zeroone.wallpaperdeal.webservice.service.UserService;
import com.zeroone.wallpaperdeal.webservice.service.WallpaperService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/1.0/users")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    WallpaperService wallpaperService;
    @PostMapping
    User saveUser(@RequestBody User user){
        UserDetail userDetail = user.getUserDetail();
        UserDetail newDetail = new UserDetail();
        if(userDetail != null){
            if(userDetail.getFollowed() != null && !(userDetail.getFollowed().isEmpty())){
                newDetail.setFollowed(user.getUserDetail().getFollowed());
            }else{
                newDetail.setFollowed(new ArrayList<>());
            }
            if(userDetail.getFollowers() != null && !(userDetail.getFollowers().isEmpty())){
                newDetail.setFollowers(user.getUserDetail().getFollowers());
            }else{
                newDetail.setFollowers(new ArrayList<>());
            }
            if(userDetail.getFavoriteWallpapers() != null && !(userDetail.getFavoriteWallpapers().isEmpty())){
                newDetail.setFavoriteWallpapers(user.getUserDetail().getFavoriteWallpapers());
            }else{
                newDetail.setFavoriteWallpapers(new ArrayList<>());
            }
        }
        if(userDetail.getProfilePhoto() == null ||  user.getUserDetail().getProfilePhoto().isEmpty()){
            newDetail.setProfilePhoto("https://firebasestorage.googleapis.com/v0/b/wall-deal.appspot.com/o/logo%2Fuser.png?alt=media&token=f38aa1f6-a721-4a83-ace9-6123c30d57a6");
        }else{
            newDetail.setProfilePhoto(user.getUserDetail().getProfilePhoto());
        }
        user.setUserDetail(newDetail);
        userService.createUser(user);
        return user;
    }
    @GetMapping("/{userId}")
    User getUser(@PathVariable String userId){
        return userService.getUser(userId);
    }

    @GetMapping("/check-user/{userId}")
    ResponseEntity<Boolean> checkUser(@PathVariable String userId) throws IOException {
        User user = getUser(userId);
        List<User> users =  userService.getUsers();
        if(users.contains(user)){
            return ResponseEntity.ok(true);
        }else{
            return ResponseEntity.ok(false);
        }
    }
    @GetMapping
    ResponseEntity<List<User>> getUsers(){
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("/check/favorites/{userId}/{wallpaperId}")
    ResponseEntity<Boolean> checkFavorites(@PathVariable String userId, @PathVariable String wallpaperId){
        User user = userService.getUser(userId);
        List<Wallpaper> favorites = user.getUserDetail().getFavoriteWallpapers();
        Wallpaper wallpaper = wallpaperService.getWallpaper(wallpaperId);
        if(favorites.contains(wallpaper)) return ResponseEntity.ok(true);
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
        try{
            userService.editUser(user);
            return new ResponseEntity<>(OK);
        }catch (RuntimeException ex){
            throw ex;
        }
    }

    @DeleteMapping("/delete-user/{userId}")
    ResponseEntity deleteUser(@PathVariable String userId){
        try {
            userService.deleteAccount(userId);
            return new ResponseEntity(OK);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}