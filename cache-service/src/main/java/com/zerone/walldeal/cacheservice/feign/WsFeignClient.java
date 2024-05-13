package com.zerone.walldeal.cacheservice.feign;

import com.zerone.walldeal.cacheservice.entity.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("${feign.walldeal-web-service.name}")
public interface WsFeignClient {

    @GetMapping("/api/1.0/wallpapers")
    ResponseEntity<ApiResponse> getWallpapers();
}
