package com.zerone.walldeal.api.service;

import com.zerone.walldeal.api.entity.Couple;
import com.zerone.walldeal.api.entity.CoupleRequest;
import com.zerone.walldeal.api.entity.WallpaperRequest;

import java.util.List;

public interface CoupleService {
    void sendCoupleRequest(String senderUserId, String receiverUserId);
    void deleteRequest(String requestId);
    Boolean checkCoupleRequest(String currentUser, String targetUser);
    void createCouple(Couple couple);
    Boolean checkCouple(String targetUserId);
    Boolean checkCoupleForBetweenUserToUser(String currentUserId, String targetUserId);
    Couple getCouple(String currentUserId);
    void sendWallpaperRequest(String currentUserId, WallpaperRequest request);
    void cancelWallpaperRequest(String currentUserId, WallpaperRequest request);
    List<CoupleRequest> getRequestsByUserId(String userId);
    void deleteRequestsFromUser(String userId);
    void cancelCouple(String userId);
    Boolean checkCoupleRequests(String userId);
    WallpaperRequest getWallpaperRequest(String requestId);

    void deleteCouple(Couple couple);
}
