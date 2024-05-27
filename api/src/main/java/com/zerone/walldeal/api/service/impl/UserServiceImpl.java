package com.zerone.walldeal.api.service.impl;

import com.zerone.walldeal.api.entity.Couple;
import com.zerone.walldeal.api.entity.User;
import com.zerone.walldeal.api.entity.Wallpaper;
import com.zerone.walldeal.api.entity.firestore.FSUserModel;
import com.zerone.walldeal.api.exceptions.UserNotFoundException;
import com.zerone.walldeal.api.repository.UserRepository;
import com.zerone.walldeal.api.repository.WallpaperRepository;
import com.zerone.walldeal.api.service.CoupleService;
import com.zerone.walldeal.api.service.UserService;
import com.zerone.walldeal.api.service.WallpaperService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

@AllArgsConstructor
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    @Lazy
    WallpaperService wallpaperService;
    @Autowired
    @Lazy
    CoupleService coupleService;
    @Override
    public void createUser(User user) {
        try {
            userRepository.save(user);
        }finally {
            Firestore db = FirestoreClient.getFirestore();
            db.collection("users").document(user.getUserId()).set(convertUserToFsUser(user));
        }
    }

    @Override
    public User getUserById(String userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public Set<User> getAllUsers() {
        Set<User> users = new java.util.HashSet<>(Collections.emptySet());
        List<User> fetchedUser = userRepository.findAll();
        users.addAll(fetchedUser);
        return users;
    }

    @Override
    public void followOrUnfollow(String currentUserId, String targetUserId) {
        User currentUser = getUserById(currentUserId);
        User targetUser = getUserById(targetUserId);
        try {
            Boolean checkFollow = checkFollow(currentUserId, targetUserId);
            if(checkFollow){
                Set<String> targetUserFollowers = targetUser.getFollowers();
                targetUserFollowers.remove(currentUserId);
                targetUser.setFollowers(targetUserFollowers);
                createUser(targetUser);

                Set<String> currentUserFollowedList = currentUser.getFollowed();
                currentUserFollowedList.remove(targetUserId);
                currentUser.setFollowed(currentUserFollowedList);
            }else{
                Set<String> targetUserFollowers = targetUser.getFollowers();
                targetUserFollowers.add(currentUserId);
                targetUser.setFollowers(targetUserFollowers);
                createUser(targetUser);

                Set<String> currentUserFollowedList = currentUser.getFollowed();
                currentUserFollowedList.add(targetUserId);
                currentUser.setFollowed(currentUserFollowedList);
                createUser(currentUser);
            }
        }catch (RuntimeException e){
            throw e;
        }
    }

    @Override
    public Boolean checkFollow(String currentUserId, String targetUserId) {
        User targetUser = getUserById(targetUserId);
        Set<String> targetUserFollowers = targetUser.getFollowers();
        try{
            if(targetUserFollowers.contains(currentUserId)){
                return true;
            }else{
                return false;
            }
        }catch (RuntimeException e){
            throw e;
        }
    }

    @Override
    public void editUser(User user) {
        try {
            List<Wallpaper> wallpapers = wallpaperService.wallpaperFindByUser(user.getUserId());
            if(!wallpapers.isEmpty() && wallpapers != null){
                for(Wallpaper wallpaper : wallpapers){
                    wallpaper.setUser(user);
                    wallpaperService.createWallpaper(wallpaper);
                }
                userRepository.save(user);
            }else{
                userRepository.save(user);
            }
        }catch (RuntimeException runtimeException){
            throw runtimeException;
        }
    }

    @Override
    public void deleteAccount(String userId) {
        User user = getUserById(userId);
        List<Wallpaper> wallpapers = wallpaperService.wallpaperFindByUser(userId);
        Set<String> followers = user.getFollowers();
        Set<String> followed = user.getFollowed();
        FSUserModel fsUserModel= convertUserToFsUser(user);
        Firestore db = FirestoreClient.getFirestore();
        try {
            if(user.getCoupleId() != null && !user.getCoupleId().isEmpty()){
                Couple couple = coupleService.getCouple(userId);
                User coupleUser;
                FSUserModel coupleFsUser;
                if(couple.getUser1().getUserId() == userId){
                    coupleUser = couple.getUser2();
                    coupleFsUser = convertUserToFsUser(couple.getUser2());
                }else{
                    coupleUser = couple.getUser1();
                    coupleFsUser = convertUserToFsUser(couple.getUser1());
                }
                coupleUser.setCoupleId("");
                createUser(coupleUser);
                coupleService.deleteCouple(couple);

                coupleFsUser.setCoupleId("");
                db.collection("users").document(coupleFsUser.getUserId()).set(coupleFsUser);
                db.collection("wallDeal").document(couple.getGroupId()).delete();
            }
            for(Wallpaper wallpaper : wallpapers){
                wallpaperService.removeWallpaper(wallpaper.getWallpaperId());
            }
            for(String id : followers){
                User followUser = getUserById(id);
                Set<String> followedList = followUser.getFollowed();
                followedList.remove(userId);
                createUser(followUser);

                FSUserModel fsUserFollow = convertUserToFsUser(getUserById(id));
                List<String> fsFollowedList = fsUserFollow.getFollowed();
                fsFollowedList.remove(userId);
                db.collection("users").document(fsUserFollow.getUserId()).set(fsUserFollow);
            }
            for (String id: followed){
                User followedUser = getUserById(id);
                Set<String> followerList = followedUser.getFollowers();
                followerList.remove(userId);
                createUser(followedUser);

                FSUserModel fsUserFollowed = convertUserToFsUser(getUserById(id));
                List<String> fsFollowerList = fsUserFollowed.getFollowers();
                followerList.remove(userId);
                db.collection("users").document(fsUserFollowed.getUserId()).set(fsUserFollowed);
            }
            userRepository.delete(user);
        }catch (RuntimeException ex){
            throw ex;
        }
    }
    public FSUserModel convertUserToFsUser(User user){
        return FSUserModel.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .username(user.getUsername())
                .fcmToken(user.getFcmToken())
                .coupleId(user.getCoupleId())
                .profilePhoto(user.getProfilePhoto())
                .addedFavorites(user.getAddedFavorites().stream().toList())
                .favoriteWallpapers(user.getFavoriteWallpapers().stream().toList())
                .followed(user.getFollowed().stream().toList())
                .followers(user.getFollowers().stream().toList())
                .build();
    }

}
