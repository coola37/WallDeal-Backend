package com.zerone.walldeal.api.service.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.zerone.walldeal.api.entity.*;
import com.zerone.walldeal.api.entity.firestore.FSCouple;
import com.zerone.walldeal.api.entity.firestore.FSCoupleRequestModel;
import com.zerone.walldeal.api.entity.firestore.FSUserModel;
import com.zerone.walldeal.api.entity.firestore.FSWallpaperRequestModel;
import com.zerone.walldeal.api.exceptions.CoupleRequestNotFoundException;
import com.zerone.walldeal.api.repository.CoupleRepository;
import com.zerone.walldeal.api.repository.CoupleRequestRepository;
import com.zerone.walldeal.api.repository.WallpaperRequestRepository;
import com.zerone.walldeal.api.service.CoupleService;
import com.zerone.walldeal.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class CoupleServiceImpl implements CoupleService {

    @Autowired
    @Lazy
    WallpaperRequestRepository wallpaperRequestRepository;
    @Autowired
    @Lazy
    UserService userService;
    @Autowired
    CoupleRepository coupleRepository;
    @Autowired
    @Lazy
    CoupleRequestRepository coupleRequestRepository;

    @Override
    public void sendCoupleRequest(String senderUserId, String receiverUserId) {
        User senderUser = userService.getUserById(senderUserId);
        User receiverUser = userService.getUserById(receiverUserId);
        CoupleRequest coupleRequest = CoupleRequest.builder()
                .coupleRequestId(senderUserId + receiverUserId)
                .title(senderUser.getUsername() + " wants to make a wall deal with you")
                .senderUser(senderUser)
                .receiverUser(receiverUser)
                .build();
        try {
            coupleRequestRepository.save(coupleRequest);
        }catch (RuntimeException e){
            throw e;
        }finally {
            FSCoupleRequestModel fsCoupleRequest = convertCoupleRequestToFsCoupleRequest(coupleRequest);
            Firestore db = FirestoreClient.getFirestore();

            db.collection("requests").document(fsCoupleRequest.getCoupleRequestId()).set(fsCoupleRequest);
        }
    }
    @Override
    public void deleteRequest(String requestId) {
        try {
            CoupleRequest request = coupleRequestRepository.findById(requestId)
                    .orElseThrow(() -> new CoupleRequestNotFoundException(requestId));
            coupleRequestRepository.delete(request);
        }catch (RuntimeException e){
            throw e;
        }finally {
            Firestore db = FirestoreClient.getFirestore();
            db.collection("requests").document(requestId).delete();
        }
    }

    @Override
    public Boolean checkCoupleRequest(String currentUser, String targetUser) {
        try {
            Optional<CoupleRequest> request1 = coupleRequestRepository.findById(currentUser+targetUser);
            Optional<CoupleRequest> request2 = coupleRequestRepository.findById(targetUser+currentUser);
            if(request1.isPresent()){
                return true;
            }else {
                return request2.isPresent();
            }
        }catch (RuntimeException e){
            throw e;
        }
    }

    @Override
    public void createCouple(Couple couple) {
        User currentUser = couple.getUser1();
        User otherUser = couple.getUser2();
        try{
            coupleRepository.save(couple);
            currentUser.setCoupleId(couple.getGroupId());
            otherUser.setCoupleId(couple.getGroupId());
            userService.createUser(currentUser);
            userService.createUser(otherUser);
            deleteRequest(couple.getGroupId());
        }catch (RuntimeException e){
            throw e;
        }finally {
            FSCouple fsCouple = convertCoupleToFsCouple(couple);
            FSUserModel fsUserModel1 = convertUserToFsUser(currentUser);
            FSUserModel fsUserModel2 = convertUserToFsUser(otherUser);
            fsUserModel1.setCoupleId(couple.getGroupId());
            fsUserModel2.setCoupleId(couple.getGroupId());

            Firestore db = FirestoreClient.getFirestore();
            db.collection("users").document(couple.getGroupId()).set(fsUserModel1);
            db.collection("users").document(couple.getGroupId()).set(fsUserModel2);
            db.collection("wallDeal").document(couple.getGroupId()).set(fsCouple);
        }
    }

    @Override
    public Boolean checkCouple(String targetUserId) {
        try {
            User targetUser = userService.getUserById(targetUserId);
            String coupleId = targetUser.getCoupleId();
            if(coupleId == null || coupleId.isEmpty()){
                return false;
            }else{
                return true;
            }
        }catch (RuntimeException ex){
            throw ex;
        }
    }

    @Override
    public Boolean checkCoupleForBetweenUserToUser(String currentUserId, String targetUserId) {
        User currentUser = userService.getUserById(currentUserId);
        String coupleId = currentUser.getCoupleId();
        if(coupleId.contains(targetUserId)){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public Couple getCouple(String currentUserId) {
        User currentUser = userService.getUserById(currentUserId);
        String coupleId = currentUser.getCoupleId();
        Couple emptyCouple = Couple.builder().build();
        try {
            if(coupleId != null && !coupleId.isEmpty()){
                Couple couple = coupleRepository.findById(coupleId)
                        .orElseThrow(() -> new CoupleRequestNotFoundException(coupleId));
                return couple;
            }else{
                return emptyCouple;
            }
        }catch (RuntimeException e){
            throw e;
        }
    }

    @Override
    public void sendWallpaperRequest(String currentUserId, WallpaperRequest request) {
        Couple couple = getCouple(currentUserId);
        try {
            couple.setRequestId(request.getWallpaperRequestId());
            coupleRepository.save(couple);
            wallpaperRequestRepository.save(request);
        } catch (RuntimeException e) {
            throw e;
        } finally {
            Firestore db = FirestoreClient.getFirestore();
            FSCouple fsCouple = FSCouple.builder()
                    .requestId(couple.getRequestId())
                    .groupId(couple.getGroupId())
                    .user1(convertUserToFsUser(couple.getUser1()))
                    .user2(convertUserToFsUser(couple.getUser2()))
                    .build();

            FSWallpaperRequestModel fsWallpaperRequestModel = FSWallpaperRequestModel.builder()
                    .wallpaperRequestId(request.getWallpaperRequestId())
                    .imageUrl(request.getImageUrl())
                    .message(request.getMessage())
                    .senderUser(convertUserToFsUser(request.getSenderUser()))
                    .receiverUser(convertUserToFsUser(request.getReceiverUser()))
                    .build();
            fsCouple.setRequestId(fsWallpaperRequestModel.getWallpaperRequestId());
            db.collection("wallDeal").document(couple.getGroupId()).set(fsCouple);
            db.collection("wallpaperRequest").document(request.getWallpaperRequestId()).set(fsWallpaperRequestModel);
        }
    }


    @Override
    public void cancelWallpaperRequest(String currentUserId, WallpaperRequest request) {
        Couple couple = getCouple(currentUserId);
        couple.setRequestId("");
        try{
            coupleRepository.save(couple);
            wallpaperRequestRepository.delete(request);
        }catch (Exception exception){
            throw exception;
        }finally {
            FSCouple fsCouple = convertCoupleToFsCouple(couple);
            Firestore db = FirestoreClient.getFirestore();
            db.collection("wallDeal").document(couple.getGroupId()).set(fsCouple);
            db.collection("wallpaperRequest").document(request.getWallpaperRequestId()).delete();
        }
    }

    @Override
    public List<CoupleRequest> getRequestsByUserId(String userId) {
        try{
            return coupleRequestRepository.findByReceiverUser_UserId(userId);
        }catch (RuntimeException e){
            throw e;
        }
    }

    @Override
    public void deleteRequestsFromUser(String userId) {
       try{
           List<CoupleRequest> requests = coupleRequestRepository.findByReceiverUser_UserId(userId);
           for(CoupleRequest request : requests){
               coupleRequestRepository.delete(request);
           }
           Firestore db = FirestoreClient.getFirestore();
           CollectionReference wallpapersRef = db.collection("wallDealRequest");

           Query query = wallpapersRef.whereEqualTo("receiverUserId.userId", userId);

           ApiFuture<QuerySnapshot> querySnapshot = query.get();

           for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
               db.collection("wallDealRequest").document(document.getId()).delete();
           }
       }catch (RuntimeException e){
           throw e;
       } catch (ExecutionException e) {
           throw new RuntimeException(e);
       } catch (InterruptedException e) {
           throw new RuntimeException(e);
       }
    }

    @Override
    public void cancelCouple(String userId) {
        Firestore db = FirestoreClient.getFirestore();
        Couple couple = getCouple(userId);
        try {
            User user1 = userService.getUserById(couple.getUser1().getUserId());
            User user2 = userService.getUserById(couple.getUser2().getUserId());
            user1.setCoupleId("");
            user2.setCoupleId("");
            userService.createUser(user1);
            userService.createUser(user2);
            coupleRepository.delete(couple);
            db.collection("users").document(user1.getUserId()).set(convertUserToFsUser(user1));
            db.collection("users").document(user2.getUserId()).set(convertUserToFsUser(user2));
            db.collection("wallDeal").document(couple.getGroupId()).delete();
        }catch (Exception exception){
            throw new RuntimeException();
        }
    }

    @Override
    public Boolean checkCoupleRequests(String userId) {
        try{
            List<CoupleRequest> requests = coupleRequestRepository.findByReceiverUser_UserId(userId);
            return !requests.isEmpty();
        }catch (RuntimeException e){
            throw e;
        }
    }

    @Override
    public WallpaperRequest getWallpaperRequest(String requestId) {
        WallpaperRequest request = wallpaperRequestRepository.findById(requestId).orElseGet(() -> null);
        return request;
    }

    @Override
    public void deleteCouple(Couple couple) {
        coupleRepository.delete(couple);
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

    FSCouple convertCoupleToFsCouple(Couple couple){
        return  FSCouple.builder()
                .requestId(couple.getRequestId())
                .groupId(couple.getGroupId())
                .user1(convertUserToFsUser(couple.getUser1()))
                .user2(convertUserToFsUser(couple.getUser2()))
                .build();
    }

    FSCoupleRequestModel convertCoupleRequestToFsCoupleRequest(CoupleRequest request){
        return FSCoupleRequestModel.builder()
                .coupleRequestId(request.getCoupleRequestId())
                .title(request.getSenderUser().getUsername() + " wants to make a wall deal with you")
                .senderUser(convertUserToFsUser(request.getSenderUser()))
                .receiverUser(convertUserToFsUser(request.getReceiverUser()))
                .build();
    }
}
