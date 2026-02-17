package com.example.map.dto;

import com.example.map.entity.Review;
import com.example.map.entity.ReviewImage;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ReviewResponse {

    private Long id;
    private String restaurantName;
    private String externalId;
    private Long userId;
    private String content;
    private Integer rating;
    private String visibility;
    private List<String> imagePaths;
    private LocalDateTime createdAt;

    public static ReviewResponse from(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getRestaurant().getName(),
                review.getRestaurant().getExternalId(),
                review.getUserId(),
                review.getContent(),
                review.getRating(),
                review.getVisibility().name(),
                review.getImages().stream().map(ReviewImage::getImagePath).toList(),
                review.getCreatedAt()
        );
    }
}
