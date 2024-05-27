package com.zerone.walldeal.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "wallpaper_requests")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WallpaperRequest {
    @Id
    @Column(name = "wallpaper_request_id")
    private String wallpaperRequestId;

    @Column(name = "message")
    private String message;

    @ManyToOne
    @JoinColumn(name = "sender_user_id", nullable = false)
    private User senderUser;

    @ManyToOne
    @JoinColumn(name = "receiver_user_id", nullable = false)
    private User receiverUser;

    @Column(name = "image_url")
    private String imageUrl;

}
