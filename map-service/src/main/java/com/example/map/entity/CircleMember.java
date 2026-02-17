package com.example.map.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "circle_members")
public class CircleMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "circle_id", nullable = false)
    private Circle circle;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime joinedAt;

    private CircleMember(Circle circle, Long userId) {
        this.circle = circle;
        this.userId = userId;
        this.joinedAt = LocalDateTime.now();
    }

    public static CircleMember create(Circle circle, Long userId) {
        return new CircleMember(circle, userId);
    }
}
