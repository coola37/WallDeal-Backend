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
public class Wallpaper {
    String wallpaperId;
    String description;
    UserDTO owner;
    String imageUrl;
    String category;
    String gradiantUrl;
    List<String> likedUser;
    Integer likeCount;
}
