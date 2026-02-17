package com.example.feed.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "feed_items")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long authorId;

    @Column(nullable = false)
    private String authorNickname;

    private String authorProfileUrl;

    @Column(nullable = false)
    private Long restaurantId;

    @Column(nullable = false)
    private String restaurantName;

    @Column(columnDefinition = "TEXT")
    private String reviewContent;

    private Integer rating;

    @Column(columnDefinition = "TEXT")
    private String imageUrls;

    @Column(nullable = false)
    private Long groupId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
