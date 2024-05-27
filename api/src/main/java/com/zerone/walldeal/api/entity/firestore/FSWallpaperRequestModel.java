package com.zerone.walldeal.api.entity.firestore;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FSWallpaperRequestModel {
    private String wallpaperRequestId;
    private String message;
    private FSUserModel senderUser;
    private FSUserModel receiverUser;
    private String imageUrl;
}
