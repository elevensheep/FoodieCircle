package com.example.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewCreatedEvent {

    private Long authorId;
    private String authorNickname;
    private String authorProfileUrl;
    private Long restaurantId;
    private String restaurantName;
    private String reviewContent;
    private Integer rating;
    private String imageUrls;
    private Long groupId;
}
