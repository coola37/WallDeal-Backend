package com.zerone.walldeal.cacheservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    String userId;
    String email;
    String username;
    String wallDealId;
    String profilePhoto;
    String fcmToken;
}
