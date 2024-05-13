package com.zeroone.wallpaperdeal.webservice.service.Impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.zeroone.wallpaperdeal.webservice.entity.*;
import com.zeroone.wallpaperdeal.webservice.service.UserService;
import com.zeroone.wallpaperdeal.webservice.service.WallDealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class WallDealServiceImpl implements WallDealService {
    @Autowired
    UserService userService;
    @Override
    public void sendWallDealRequest(String senderUserId, String receiverUserId) {
        try {
            User sender = userService.getUser(senderUserId);
            User receiver = userService.getUser(receiverUserId);
            WallDeal possibleWallDeal = WallDeal.builder()
                    .user1(userService.userConvertUserDTO(sender))
                    .user2(userService.userConvertUserDTO(receiver))
                    .groupId(senderUserId + receiverUserId)
                    .build();

            WallDealRequest wallDealRequest = WallDealRequest.builder()
                    .wallDealRequestId(senderUserId + receiverUserId)
                    .title(sender.getUsername() + " wants to make a wall deal with you")
                    .senderUser(userService.userConvertUserDTO(sender))
                    .receiverUser(userService.userConvertUserDTO(receiver))
                    .possibleWallDeal(possibleWallDeal)
                    .build();

            Firestore db = FirestoreClient.getFirestore();
            db.collection("requests").document(wallDealRequest.getWallDealRequestId()).set(wallDealRequest);
        }catch (RuntimeException exception){
            throw exception;
        }
    }

    @Override
    public void deleteRequest(String requestId) {
        try {
            Firestore db = FirestoreClient.getFirestore();
            db.collection("requests").document(requestId).delete();
        }catch (Exception exception){
            throw new RuntimeException();
        }
    }

    @Override
    public Boolean checkWallDealRequest(String currentUser, String targetUser) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<DocumentSnapshot> future = db.collection("requests").document(currentUser+targetUser).get();
        ApiFuture<DocumentSnapshot> futureOptional = db.collection("requests").document(targetUser+currentUser).get();
        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                return true;
            } else {
                DocumentSnapshot documentOptional = futureOptional.get();
                return documentOptional.exists();
            }
        } catch (InterruptedException | ExecutionException e) {
            throw e;
        }
    }

    @Override
    public void createWallDeal(WallDeal wallDeal) {
        try{
            UserDTO currentUserDTO = wallDeal.getUser1();
            UserDTO otherUserDTO = wallDeal.getUser2();
            Firestore db = FirestoreClient.getFirestore();
            db.collection("wallDeal").document(wallDeal.getGroupId()).set(wallDeal);

            User currentUser = userService.getUser(currentUserDTO.getUserId());
            User otherUser = userService.getUser(otherUserDTO.getUserId());
            currentUser.setWallDealId(wallDeal.getGroupId());
            otherUser.setWallDealId(wallDeal.getGroupId());
            db.collection("users").document(currentUser.getUserId()).set(currentUser);
            db.collection("users").document(otherUser.getUserId()).set(otherUser);
            deleteRequest(wallDeal.getGroupId());
        }catch (Exception e){
            throw new RuntimeException();
        }
    }

    @Override
    public Boolean checkWallDeal(String targetUserId) {
        User targetUser = userService.getUser(targetUserId);
        String wallDealId = targetUser.getWallDealId();
        if(wallDealId == null || wallDealId.isEmpty()){
            return false;
        }else{
            return true;
        }
    }

    @Override
    public Boolean checkWallDealForBetweenUserToUser(String currentUserId, String targetUserId) {
        User currentUser = userService.getUser(currentUserId);
        String wallDealId = currentUser.getWallDealId();
        if(wallDealId.contains(targetUserId)){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public WallDeal getWallDeal(String currentUserId) throws ExecutionException, InterruptedException {
        User currentUser = userService.getUser(currentUserId);
        String wallDealId = currentUser.getWallDealId();
        Firestore db = FirestoreClient.getFirestore();
        WallDeal emptyWalldeal = WallDeal.builder()
                .build();
        if (wallDealId != null && !wallDealId.isEmpty()){
            ApiFuture<DocumentSnapshot> future = db.collection("wallDeal").document(wallDealId).get();
            try {
                DocumentSnapshot document = future.get();
                if (document.exists()) {
                    return document.toObject(WallDeal.class);
                } else {
                    System.err.println(wallDealId);
                    return null;
                }
            } catch (InterruptedException | ExecutionException e) {
                throw e;
            }
        }else{
            return emptyWalldeal;
        }
    }

    @Override
    public void addUserToWallDeal(String userId, String otherUserId) throws IOException {
       /* try{
            User currentUser = userService.getUser(userId);
            User otherUser = userService.getUser(otherUserId);

            Firestore db = FirestoreClient.getFirestore();

            WallDeal wallDeal = getWallDeal(currentUser.getWallDealId());
            if(wallDeal.getGroupId() != null && !wallDeal.getGroupId().isEmpty()){
                String oldWallDealId = wallDeal.getGroupId();
                wallDeal.setGroupId(wallDeal.getGroupId() + otherUserId);
                List<UserDTO> members = wallDeal.getGroupMembers();
                members.add(userService.userConvertUserDTO(otherUser));
                wallDeal.setGroupMembers(members);
                db.collection("wallDeal").document(oldWallDealId).set(wallDeal);
            }else{
                createWallDeal(wallDeal);
            }
        }catch (Exception e){
            throw new IOException();
        }*/
    }

    @Override
    public void sendWallpaperRequest(String currentUserId, WallpaperRequest request) throws ExecutionException, InterruptedException {
        WallDeal wallDeal = getWallDeal(currentUserId);
        Firestore db = FirestoreClient.getFirestore();

        try {
            wallDeal.setRequestId(request.getWallpaperRequestId());
            db.collection("wallDeal").document(wallDeal.getGroupId()).set(wallDeal);
            db.collection("wallpaperRequest").document(request.getWallpaperRequestId()).set(request);
        }catch (Exception exception){
            throw exception;
        }
    }

    @Override
    public void cancelWallpaperRequest(String currentUserId, WallpaperRequest request) throws ExecutionException, InterruptedException {
        WallDeal wallDeal = getWallDeal(currentUserId);
        Firestore db = FirestoreClient.getFirestore();
        try{
            wallDeal.setRequestId("");
            db.collection("wallDeal").document(wallDeal.getGroupId()).set(wallDeal);
            db.collection("wallpaperRequest").document(request.getWallpaperRequestId()).delete();
        }catch (Exception exception){
            throw exception;
        }
    }

    @Override
    public List<WallDealRequest> getRequestsByUserId(String userId) {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference wallpapersRef = db.collection("requests");

        Query query = wallpapersRef.whereEqualTo("receiverUser.userId", userId);

        ApiFuture<QuerySnapshot> future = query.get();

        List<WallDealRequest> requests = new ArrayList<>();

        try {
            QuerySnapshot querySnapshot = future.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();

            for (QueryDocumentSnapshot document : documents) {
                WallDealRequest request = document.toObject(WallDealRequest.class);
                requests.add(request);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return requests;
    }

    @Override
    public void deleteRequestsFromUser(String userId) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference wallpapersRef = db.collection("wallDealRequest");

        Query query = wallpapersRef.whereEqualTo("receiverUserId.userId", userId);

        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            db.collection("wallDealRequest").document(document.getId()).delete();
        }
    }

    @Override
    public void cancelWallDeal(String userId) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        WallDeal wallDeal = getWallDeal(userId);
        try {
            User user1 = userService.getUser(wallDeal.getUser1().getUserId());
            User user2 = userService.getUser(wallDeal.getUser2().getUserId());
            user1.setWallDealId("");
            user2.setWallDealId("");
            db.collection("users").document(user1.getUserId()).set(user1);
            db.collection("users").document(user2.getUserId()).set(user2);
            db.collection("wallDeal").document(wallDeal.getGroupId()).delete();
        }catch (Exception exception){
            throw new RuntimeException();
        }
    }

    @Override
    public Boolean checkWallDealRequests(String userId) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference wallpapersRef = db.collection("requests");
        Query query = wallpapersRef.whereEqualTo("receiverUser.userId", userId);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        try {
            return !querySnapshot.get().isEmpty();
        } catch (InterruptedException | ExecutionException e) {
            throw e;
        }
    }

    @Override
    public WallpaperRequest getWallpaperRequest(String requestId) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<DocumentSnapshot> future = db.collection("wallpaperRequest").document(requestId).get();
        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                return document.toObject(WallpaperRequest.class);
            } else {
                System.err.println(requestId);
                return null;
            }
        } catch (InterruptedException | ExecutionException e) {
            throw e;
        }
    }
}
