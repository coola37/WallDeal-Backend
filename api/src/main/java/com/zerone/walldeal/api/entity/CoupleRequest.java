package com.zerone.walldeal.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@Table(name = "couple_requests")
@AllArgsConstructor
@NoArgsConstructor
public class CoupleRequest {
    @Id
    @Column(name = "couple_request_id", nullable = false, unique = true)
    private String coupleRequestId;

    @Column(name = "title")
    private String title;

    @ManyToOne
    @JoinColumn(name = "sender_user_id", nullable = false)
    private User senderUser;

    @ManyToOne
    @JoinColumn(name = "receiver_user_id", nullable = false)
    private User receiverUser;
}
