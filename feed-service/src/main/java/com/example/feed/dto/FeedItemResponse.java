package com.example.feed.dto;

import com.example.feed.entity.FeedItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class FeedItemResponse {

    private Long id;
    private Long authorId;
    private String authorNickname;
    private String authorProfileUrl;
    private Long restaurantId;
    private String restaurantName;
    private String reviewContent;
    private Integer rating;
    private List<String> imageUrls;
    private Long groupId;
    private LocalDateTime createdAt;

    public static FeedItemResponse from(FeedItem feedItem) {
        return FeedItemResponse.builder()
                .id(feedItem.getId())
                .authorId(feedItem.getAuthorId())
                .authorNickname(feedItem.getAuthorNickname())
                .authorProfileUrl(feedItem.getAuthorProfileUrl())
                .restaurantId(feedItem.getRestaurantId())
                .restaurantName(feedItem.getRestaurantName())
                .reviewContent(feedItem.getReviewContent())
                .rating(feedItem.getRating())
                .imageUrls(parseImageUrls(feedItem.getImageUrls()))
                .groupId(feedItem.getGroupId())
                .createdAt(feedItem.getCreatedAt())
                .build();
    }

    private static List<String> parseImageUrls(String imageUrls) {
        if (imageUrls == null || imageUrls.isBlank()) {
            return Collections.emptyList();
        }
        return Arrays.asList(imageUrls.split(","));
    }
}
