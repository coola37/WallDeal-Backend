package com.zeroone.wallpaperdeal.webservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WallpaperRequest {
    String wallpaperRequestId;
    String message;
    UserDTO senderUser;
    UserDTO receiverUser;
    String imageUrl;
}
