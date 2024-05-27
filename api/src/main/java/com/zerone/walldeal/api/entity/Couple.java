package com.zerone.walldeal.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "couples")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Couple {
    @Id
    @Column(name = "group_id", nullable = false, unique = true)
    private String groupId;

    @ManyToOne
    @JoinColumn(name = "user_id_1", nullable = false)
    private User user1;

    @ManyToOne
    @JoinColumn(name = "user_id_2", nullable = false)
    private User user2;

    @Column(name = "request_id")
    private String requestId;
}
