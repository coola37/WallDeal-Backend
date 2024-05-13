package com.zeroone.wallpaperdeal.webservice.controller;

import com.zeroone.wallpaperdeal.webservice.entity.WallDeal;
import com.zeroone.wallpaperdeal.webservice.entity.WallDealRequest;
import com.zeroone.wallpaperdeal.webservice.entity.Wallpaper;
import com.zeroone.wallpaperdeal.webservice.entity.WallpaperRequest;
import com.zeroone.wallpaperdeal.webservice.service.WallDealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/1.0/walldeal")
public class WallDealController {
    @Autowired
    WallDealService service;

    @PostMapping("/send-request/{senderUser}/{receiverUser}")
    void sendRequest(@PathVariable String senderUser, @PathVariable String receiverUser){
        try {
            service.sendWallDealRequest(senderUser, receiverUser);
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
            return ResponseEntity.ok(service.checkWallDealRequest(currentUserId, targetUserId));
        }catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/check-walldeal/{targetUserId}")
    ResponseEntity<Boolean> checkWallDealRequest(@PathVariable String targetUserId){
        try {
            return ResponseEntity.ok(service.checkWallDeal(targetUserId));
        }catch (RuntimeException e){
            throw e;
        }
    }

    @GetMapping("/check-walldeal-for-between-user-to-user/{currentUserId}/{targetUserId}")
    ResponseEntity<Boolean> checkWallDealForBetweenUserToUser(@PathVariable String currentUserId, @PathVariable String targetUserId){
        try {
            return ResponseEntity.ok(service.checkWallDealForBetweenUserToUser(currentUserId, targetUserId));
        }catch (RuntimeException e){
            throw e;
        }
    }

    @PutMapping("/send-post/{currentUserId}")
    void sendPost(@PathVariable String currentUserId, @RequestBody WallpaperRequest request){
        try {
            service.sendWallpaperRequest(currentUserId, request);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/cancel-post/{currentUserId}")
    void cancelPost(@PathVariable String currentUserId, @RequestBody WallpaperRequest request){
        try {
            service.cancelWallpaperRequest(currentUserId, request);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/get-walldeal-request/{userId}")
    ResponseEntity<List<WallDealRequest>> getRequestsByUserId(@PathVariable String userId){
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
        }catch (RuntimeException e){
            throw e;
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/request-notification-check/{userId}")
    ResponseEntity<Boolean> checkRequestNotifications(@PathVariable String userId){
        try {
            return ResponseEntity.ok(service.checkWallDealRequests(userId));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/create-walldeal")
     ResponseEntity createWallDeal(@RequestBody WallDeal wallDeal){
        try {
            service.createWallDeal(wallDeal);
            return new ResponseEntity<>(OK);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/get-walldeal/{userId}")
    ResponseEntity<WallDeal> getWallDeal(@PathVariable String userId){
        try{
            return ResponseEntity.ok(service.getWallDeal(userId));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/cancel-walldeal/{userId}")
    ResponseEntity cancelWallDeal(@PathVariable String userId){
        try {
            service.cancelWallDeal(userId);
            return new ResponseEntity<>(OK);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/add-user-to-walldeal/{userId}/{otherUserId}")
    ResponseEntity addUserToWalldeal(@PathVariable String userId, @PathVariable String otherUserId){
        try {
            service.addUserToWallDeal(userId, otherUserId);
            return new ResponseEntity(OK);
        }catch (RuntimeException ex){
            throw ex;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}