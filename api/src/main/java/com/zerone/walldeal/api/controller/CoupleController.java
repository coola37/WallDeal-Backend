package com.zerone.walldeal.api.controller;

import com.zerone.walldeal.api.entity.Couple;
import com.zerone.walldeal.api.entity.CoupleRequest;
import com.zerone.walldeal.api.entity.WallpaperRequest;
import com.zerone.walldeal.api.service.CoupleService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/1.0/walldeal")
@RequiredArgsConstructor
public class CoupleController {
    @Autowired
    CoupleService service;

    @PostMapping("/send-request/{senderUser}/{receiverUser}")
    void sendRequest(@PathVariable String senderUser, @PathVariable String receiverUser){
        try {
            service.sendCoupleRequest(senderUser, receiverUser);
        }catch (RuntimeException e){
            throw e;
        }
    }

    @DeleteMapping("/delete-request/{requestId}")
    ResponseEntity deleteRequest(@PathVariable String requestId){
        try{
            service.deleteRequest(requestId);
            return new ResponseEntity<>(OK);
        }catch (RuntimeException e){
            throw e;
        }
    }

    @GetMapping("/check-walldeal-request/{currentUserId}/{targetUserId}")
    ResponseEntity<Boolean> checkWallDealRequest(@PathVariable String currentUserId, @PathVariable String targetUserId){
        try {
            return ResponseEntity.ok(service.checkCoupleRequest(currentUserId, targetUserId));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/check-walldeal/{targetUserId}")
    ResponseEntity<Boolean> checkWallDealRequest(@PathVariable String targetUserId){
        try {
            return ResponseEntity.ok(service.checkCouple(targetUserId));
        }catch (RuntimeException e){
            throw e;
        }
    }

    @GetMapping("/check-walldeal-for-between-user-to-user/{currentUserId}/{targetUserId}")
    ResponseEntity<Boolean> checkWallDealForBetweenUserToUser(@PathVariable String currentUserId, @PathVariable String targetUserId){
        try {
            return ResponseEntity.ok(service.checkCoupleForBetweenUserToUser(currentUserId, targetUserId));
        }catch (RuntimeException e){
            throw e;
        }
    }

    @PutMapping("/send-post/{currentUserId}")
    void sendPost(@PathVariable String currentUserId, @RequestBody WallpaperRequest request){
        try {
            service.sendWallpaperRequest(currentUserId, request);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/cancel-post/{currentUserId}")
    void cancelPost(@PathVariable String currentUserId, @RequestBody WallpaperRequest request){
        try {
            service.cancelWallpaperRequest(currentUserId, request);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/get-walldeal-request/{userId}")
    ResponseEntity<List<CoupleRequest>> getRequestsByUserId(@PathVariable String userId){
        try {
            return ResponseEntity.ok(service.getRequestsByUserId(userId));
        }catch (RuntimeException e){
            throw e;
        }
    }

    @GetMapping("/get-wallpaper-request/{requestId}")
    ResponseEntity<WallpaperRequest> getWallpaperRequest(@PathVariable String requestId){
        try {
            return ResponseEntity.ok(service.getWallpaperRequest(requestId));
        }catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/request-notification-check/{userId}")
    ResponseEntity<Boolean> checkRequestNotifications(@PathVariable String userId){
        try {
            return ResponseEntity.ok(service.checkCoupleRequests(userId));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/create-walldeal")
    ResponseEntity createWallDeal(@RequestBody Couple couple){
        try {
            service.createCouple(couple);
            return new ResponseEntity<>(OK);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/get-walldeal/{userId}")
    ResponseEntity<Couple> getWallDeal(@PathVariable String userId){
        try{
            return ResponseEntity.ok(service.getCouple(userId));
        }catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/cancel-walldeal/{userId}")
    ResponseEntity cancelWallDeal(@PathVariable String userId){
        try {
            service.cancelCouple(userId);
            return new ResponseEntity<>(OK);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

}
