package com.example.map.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "review_images")
public class ReviewImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @Column(nullable = false, length = 500)
    private String imagePath;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private ReviewImage(Review review, String imagePath) {
        this.review = review;
        this.imagePath = imagePath;
        this.createdAt = LocalDateTime.now();
    }

    public static ReviewImage create(Review review, String imagePath) {
        return new ReviewImage(review, imagePath);
    }
}
