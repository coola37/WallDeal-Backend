package com.zeroone.wallpaperdeal.webservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.google.cloud.firestore.annotation.Exclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    String userId;
    String email;
    String username;
    String wallDealId;
    UserDetail userDetail;
    String fcmToken;
}
/*
    var userId: String,
    var email: String,
    var username: String,
    var wallDealId: String?,
    var userDetail: UserDetail?,
    var fcmToken: String?
 */