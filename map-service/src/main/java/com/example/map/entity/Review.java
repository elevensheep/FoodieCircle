package com.example.map.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "reviews")
public class Review {

    public enum Visibility {
        PUBLIC, GROUP
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 2000)
    private String content;

    @Column(nullable = false)
    private Integer rating;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Visibility visibility;

    @Column
    private Long groupId;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewImage> images = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private Review(Restaurant restaurant, Long userId, String content, Integer rating, Visibility visibility, Long groupId) {
        this.restaurant = restaurant;
        this.userId = userId;
        this.content = content;
        this.rating = rating;
        this.visibility = visibility;
        this.groupId = groupId;
        this.createdAt = LocalDateTime.now();
    }

    public static Review create(Restaurant restaurant, Long userId, String content, Integer rating, Visibility visibility, Long groupId) {
        return new Review(restaurant, userId, content, rating, visibility, groupId);
    }

    public void addImage(ReviewImage image) {
        this.images.add(image);
    }
}
