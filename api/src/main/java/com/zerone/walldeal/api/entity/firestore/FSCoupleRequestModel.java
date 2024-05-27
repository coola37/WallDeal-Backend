package com.zerone.walldeal.api.entity.firestore;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FSCoupleRequestModel {

    private String coupleRequestId;
    private String title;
    private FSUserModel senderUser;
    private FSUserModel receiverUser;
}
