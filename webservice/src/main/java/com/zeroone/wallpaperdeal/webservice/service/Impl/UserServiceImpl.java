package com.zeroone.wallpaperdeal.webservice.service.Impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.zeroone.wallpaperdeal.webservice.entity.*;
import com.zeroone.wallpaperdeal.webservice.service.UserService;
import com.zeroone.wallpaperdeal.webservice.service.WallDealService;
import com.zeroone.wallpaperdeal.webservice.service.WallpaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class UserServiceImpl implements UserService{

    @Lazy
    @Autowired
    WallpaperService wallpaperService;

    @Lazy
    @Autowired
    WallDealService wallDealService;
    @Override
    public User createUser(User user) {
        Firestore db = FirestoreClient.getFirestore();
        db.collection("users").document(user.getUserId()).set(user);
        return user;
    }

    @Override
    public User getUser(String userId) {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<DocumentSnapshot> future = db.collection("users").document(userId).get();
        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                return document.toObject(User.class);
            } else {
                return null;
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<User> getUsers() {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference usersRef = db.collection("users");

        ApiFuture<QuerySnapshot> future = usersRef.get();

        List<User> users = new ArrayList<>();

        try {
            QuerySnapshot querySnapshot = future.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();

            for (QueryDocumentSnapshot document : documents) {
                User user = document.toObject(User.class);
                users.add(user);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void followOrUnfollow(String currentUserId, String targetUserId) {
        User currentUser = getUser(currentUserId);
        User targetUser = getUser(targetUserId);
        Firestore db = FirestoreClient.getFirestore();
        try {
            Boolean check = checkFollow(currentUserId, targetUserId);
            if(check){
                UserDetail targetUserDetail = targetUser.getUserDetail();
                List<UserDTO> targetUserFollowersList = targetUserDetail.getFollowers();
                targetUserFollowersList.remove(userConvertUserDTO(currentUser));
                targetUserDetail.setFollowers(targetUserFollowersList);
                targetUser.setUserDetail(targetUserDetail);

                UserDetail currentUserDetail = currentUser.getUserDetail();
                List<UserDTO> currentUserFollowedList = currentUserDetail.getFollowed();
                currentUserFollowedList.remove(userConvertUserDTO(targetUser));
                currentUserDetail.setFollowed(currentUserFollowedList);
                currentUser.setUserDetail(currentUserDetail);

                db.collection("users").document(targetUserId).set(targetUser);
                db.collection("users").document(currentUserId).set(currentUser);
            }else{
                UserDetail detail = targetUser.getUserDetail();
                List<UserDTO> followersList = detail.getFollowers();
                followersList.add(userConvertUserDTO(currentUser));
                detail.setFollowers(followersList);
                targetUser.setUserDetail(detail);

                UserDetail currentUserDetail = currentUser.getUserDetail();
                List<UserDTO> currentUserFollowedList = currentUserDetail.getFollowed();
                currentUserFollowedList.add(userConvertUserDTO(targetUser));
                currentUserDetail.setFollowed(currentUserFollowedList);
                currentUser.setUserDetail(currentUserDetail);

                db.collection("users").document(targetUserId).set(targetUser);
                db.collection("users").document(currentUserId).set(currentUser);
            }
        }catch (FirestoreException ex){
            System.err.println(ex);
            throw ex;
        }
    }

    @Override
    public Boolean checkFollow(String currentUserId, String targetUserId) {
        User currentUser = getUser(currentUserId);
        User targetUser = getUser(targetUserId);

        try{
            if(targetUser.getUserDetail().getFollowers().contains(userConvertUserDTO(currentUser))){
                return true;
            }else{
                return false;
            }
        }catch (RuntimeException ex){
            throw ex;
        }
    }
    @Override
    public void editUser(User user) {
        Firestore db = FirestoreClient.getFirestore();
        try{
            List<Wallpaper> wallpapers = wallpaperService.getWallpaperByOwner(user.getUserId());
            if(!wallpapers.isEmpty() && wallpapers != null){
                for(Wallpaper wallpaper : wallpapers){
                    UserDTO owner = userConvertUserDTO(user);
                    owner.setUsername(owner.getUsername());
                    owner.setProfilePhoto(owner.getProfilePhoto());
                    wallpaper.setOwner(owner);
                    db.collection("wallpapers").document(wallpaper.getWallpaperId()).set(wallpaper);
                }
                db.collection("users").document(user.getUserId()).set(user);
            }else{
                db.collection("users").document(user.getUserId()).set(user);
            }
        }catch (RuntimeException exception){
            throw exception;
        }
    }

    @Override
    public void deleteAccount(String userId) throws ExecutionException, InterruptedException {
        User user = getUser(userId);
        List<Wallpaper> wallpapers = wallpaperService.getWallpaperByOwner(userId);
        List<UserDTO> followers = user.getUserDetail().getFollowers();
        List<UserDTO> followed = user.getUserDetail().getFollowed();
        Firestore db = FirestoreClient.getFirestore();
        try {
            if(user.getWallDealId() != null && !user.getWallDealId().isEmpty()){
                WallDeal wallDeal = wallDealService.getWallDeal(user.getWallDealId());
                UserDTO coupleDto;
                if(wallDeal.getUser1().getUserId() == userId){
                    coupleDto = wallDeal.getUser2();
                }else{
                    coupleDto = wallDeal.getUser1();
                }
                User couple = getUser(coupleDto.getUserId());
                user.setWallDealId("");
                db.collection("users").document(couple.getUserId()).set(couple);
                db.collection("wallDeal").document(wallDeal.getGroupId()).delete();
                for(Wallpaper wallpaper : wallpapers){
                    db.collection("wallpapers").document(wallpaper.getWallpaperId()).delete();
                }
                for(UserDTO dto : followers){
                    User follower = getUser(dto.getUserId());
                    List<UserDTO> followedList = user.getUserDetail().getFollowed();
                    followedList.remove(userConvertUserDTO(user));
                }
                for(UserDTO dto : followed){
                    User follow = getUser(dto.getUserId());
                    List<UserDTO> followerList = user.getUserDetail().getFollowers();
                    followerList.remove(userConvertUserDTO(user));
                }
                db.collection("users").document(user.getUserId()).delete();
            }else{
                for (Wallpaper wallpaper : wallpapers) {
                    db.collection("wallpapers").document(wallpaper.getWallpaperId()).delete();
                }
                for (UserDTO dto : followers) {
                    User follower = getUser(dto.getUserId());
                    List<UserDTO> followedList = user.getUserDetail().getFollowed();
                    followedList.remove(userConvertUserDTO(user));
                }
                for (UserDTO dto : followed) {
                    User follow = getUser(dto.getUserId());
                    List<UserDTO> followerList = user.getUserDetail().getFollowers();
                    followerList.remove(userConvertUserDTO(user));
                }
                db.collection("users").document(user.getUserId()).delete();
            }
        }catch (RuntimeException e){
            throw e;
        }
    }

    @Override
    public UserDTO userConvertUserDTO(User user){
        UserDTO dto = UserDTO.builder()
                .email(user.getEmail())
                .userId(user.getUserId())
                .username(user.getUsername())
                .profilePhoto(user.getUserDetail().getProfilePhoto())
                .wallDealId(user.getWallDealId())
                .fcmToken(user.getFcmToken())
                .build();
        return dto;
    }
}