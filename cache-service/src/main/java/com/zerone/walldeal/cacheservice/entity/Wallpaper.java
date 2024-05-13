package com.zerone.walldeal.cacheservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("wallpapers")
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