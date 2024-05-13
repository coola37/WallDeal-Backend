package com.zeroone.wallpaperdeal.webservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WallDealRequest {
    String wallDealRequestId;
    String title;
    UserDTO senderUser;
    UserDTO receiverUser;
    WallDeal possibleWallDeal;
}
