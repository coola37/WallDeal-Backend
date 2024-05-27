package com.zerone.walldeal.api.entity.firestore;

import com.zerone.walldeal.api.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FSCouple {
    private String groupId;
    private FSUserModel user1;
    private FSUserModel user2;
    private String requestId;
}
