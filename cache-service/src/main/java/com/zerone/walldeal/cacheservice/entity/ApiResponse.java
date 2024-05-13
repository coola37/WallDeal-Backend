package com.zerone.walldeal.cacheservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ApiResponse<T> {
    private String response;
    private T payload;
}
