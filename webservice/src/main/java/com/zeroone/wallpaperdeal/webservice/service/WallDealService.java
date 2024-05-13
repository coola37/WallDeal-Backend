package com.zeroone.wallpaperdeal.webservice.service;

import com.zeroone.wallpaperdeal.webservice.entity.WallDeal;
import com.zeroone.wallpaperdeal.webservice.entity.WallDealRequest;
import com.zeroone.wallpaperdeal.webservice.entity.Wallpaper;
import com.zeroone.wallpaperdeal.webservice.entity.WallpaperRequest;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface WallDealService {

    void sendWallDealRequest(String senderUser, String receiverUser);
    void deleteRequest(String requestId);
    Boolean checkWallDealRequest(String currentUser, String targetUser) throws ExecutionException, InterruptedException;
    void createWallDeal(WallDeal wallDeal);
    Boolean checkWallDeal(String targetUserId);
    Boolean checkWallDealForBetweenUserToUser(String currentUserId, String targetUserId);
    WallDeal getWallDeal(String wallDealId) throws ExecutionException, InterruptedException;
    void addUserToWallDeal(String userId, String otherUserId) throws IOException;
    void sendWallpaperRequest(String currentUserId, WallpaperRequest request) throws ExecutionException, InterruptedException;

    void cancelWallpaperRequest(String currentUserId, WallpaperRequest request) throws ExecutionException, InterruptedException;
    List<WallDealRequest> getRequestsByUserId(String userId);
    void deleteRequestsFromUser(String userId) throws ExecutionException, InterruptedException;
    void cancelWallDeal(String userId) throws ExecutionException, InterruptedException;
    Boolean checkWallDealRequests(String userId) throws ExecutionException, InterruptedException;
    WallpaperRequest getWallpaperRequest(String requestId) throws ExecutionException, InterruptedException;


}
