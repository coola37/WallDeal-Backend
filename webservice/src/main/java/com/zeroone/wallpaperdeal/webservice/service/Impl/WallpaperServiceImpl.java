package com.zeroone.wallpaperdeal.webservice.service.Impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.zeroone.wallpaperdeal.webservice.entity.User;
import com.zeroone.wallpaperdeal.webservice.entity.UserDTO;
import com.zeroone.wallpaperdeal.webservice.entity.UserDetail;
import com.zeroone.wallpaperdeal.webservice.entity.Wallpaper;
import com.zeroone.wallpaperdeal.webservice.service.UserService;
import com.zeroone.wallpaperdeal.webservice.service.WallpaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class WallpaperServiceImpl implements WallpaperService {
    @Autowired
    UserService userService;
    @Override
    public Wallpaper createWallpaper(Wallpaper wallpaper) {
        Firestore db = FirestoreClient.getFirestore();
        db.collection("wallpapers").document(wallpaper.getWallpaperId()).set(wallpaper);
        return wallpaper;
    }

    @Override
    public Wallpaper getWallpaper(String wallpaperId) {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<DocumentSnapshot> future = db.collection("wallpapers").document(wallpaperId).get();
        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                return document.toObject(Wallpaper.class);
            } else {
                return null;
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Wallpaper> getWallpapers() {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference wallpapersRef = db.collection("wallpapers");

        ApiFuture<QuerySnapshot> future = wallpapersRef.get();

        List<Wallpaper> wallpapers = new ArrayList<>();

        try {
            QuerySnapshot querySnapshot = future.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();

            for (QueryDocumentSnapshot document : documents) {
                Wallpaper wallpaper = document.toObject(Wallpaper.class);
                wallpapers.add(wallpaper);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return wallpapers;
    }

    @Override
    public List<Wallpaper> getWallpaperByOwner(String owner) {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference wallpapersRef = db.collection("wallpapers");


        Query query = wallpapersRef.whereEqualTo("owner.userId", owner);

        ApiFuture<QuerySnapshot> future = query.get();

        List<Wallpaper> wallpapers = new ArrayList<>();

        try {
            QuerySnapshot querySnapshot = future.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();

            for (QueryDocumentSnapshot document : documents) {
                Wallpaper wallpaper = document.toObject(Wallpaper.class);
                wallpapers.add(wallpaper);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return wallpapers;
    }

    @Override
    public List<Wallpaper> getWallpapersByFollowed(String currentUserId) {
        User currentUser = userService.getUser(currentUserId);

        List<Wallpaper> wallpapersByFollowed = new ArrayList<>();
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference wallpapersRef = db.collection("wallpapers");
        List<Wallpaper> wallpapersSortLike = new ArrayList<>();
        try{
            if(currentUser.getUserDetail().getFollowed().isEmpty() ||
                    currentUser.getUserDetail().getFollowed() == null)
            {
                wallpapersSortLike = getWallpapers();
                wallpapersSortLike = wallpapersSortLike.stream()
                        .sorted(Comparator.comparing(Wallpaper::getLikeCount).reversed())
                        .limit(50)
                        .toList();

                return wallpapersSortLike;
            }else{
                List<UserDTO> followedList = currentUser.getUserDetail().getFollowed();
                for(UserDTO user : followedList ){

                    Query query = wallpapersRef.whereEqualTo("owner", user);
                    ApiFuture<QuerySnapshot> future = query.get();
                    QuerySnapshot querySnapshot = future.get();
                    List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
                    for (QueryDocumentSnapshot document : documents){
                        Wallpaper wallpaper = document.toObject(Wallpaper.class);
                        wallpapersByFollowed.add(wallpaper);
                    }
                }
                wallpapersSortLike = getWallpapers();
                wallpapersSortLike = wallpapersSortLike.stream()
                        .sorted(Comparator.comparing(Wallpaper::getLikeCount).reversed())
                        .limit(50)
                        .toList();
                for (Wallpaper wallpaper : wallpapersSortLike) {
                    if (!wallpapersByFollowed.contains(wallpaper)) {
                        wallpapersByFollowed.add(wallpaper);
                    }
                }
                return wallpapersByFollowed;
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeWallpaper(String wallpaperId) {
        Firestore db = FirestoreClient.getFirestore();
        try{
            db.collection("wallpapers").document(wallpaperId).delete();
        }catch (RuntimeException ex){
            throw ex;
        }
    }

    @Override
    public List<Wallpaper> getWallpapersByCategory(String categoryName) {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference wallpapersRef = db.collection("wallpapers");
        Query query = wallpapersRef.whereEqualTo("category", categoryName);

        ApiFuture<QuerySnapshot> future = query.get();

        List<Wallpaper> wallpapers = new ArrayList<>();

        try {
            QuerySnapshot querySnapshot = future.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();

            for (QueryDocumentSnapshot document : documents) {
                Wallpaper wallpaper = document.toObject(Wallpaper.class);
                wallpapers.add(wallpaper);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return wallpapers;
    }
    @Override
    public void addFavoritesWallpaper(String userId, String wallpaperId){
        try{
            User user = userService.getUser(userId);
            Wallpaper wallpaper = getWallpaper(wallpaperId);
            UserDetail detail = user.getUserDetail();
            List<Wallpaper> favorites = detail.getFavoriteWallpapers();
            if(favorites.contains(wallpaper)){
                favorites.remove(wallpaper);
            }else{
                favorites.add(wallpaper);
            }
            detail.setFavoriteWallpapers(favorites);
            user.setUserDetail(detail);
            userService.createUser(user);
        }catch (Exception e){
            throw new RuntimeException();
        }
    }
}
