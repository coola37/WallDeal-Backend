package com.zeroone.wallpaperdeal.webservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WallDeal {
    String groupId;
    UserDTO user1;
    UserDTO user2;
    String requestId;
}
