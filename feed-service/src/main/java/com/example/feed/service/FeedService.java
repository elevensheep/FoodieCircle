package com.example.feed.service;

import com.example.common.dto.ReviewCreatedEvent;
import com.example.feed.dto.FeedItemResponse;
import com.example.feed.entity.FeedItem;
import com.example.feed.repository.FeedItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final FeedItemRepository feedItemRepository;

    public Page<FeedItemResponse> getFeed(Long groupId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<FeedItem> feedItems;
        if (groupId != null) {
            feedItems = feedItemRepository.findByGroupIdOrderByCreatedAtDesc(groupId, pageable);
        } else {
            feedItems = feedItemRepository.findAllByOrderByCreatedAtDesc(pageable);
        }

        return feedItems.map(FeedItemResponse::from);
    }

    public void createFeedItem(ReviewCreatedEvent event) {
        FeedItem feedItem = FeedItem.builder()
                .authorId(event.getAuthorId())
                .authorNickname(event.getAuthorNickname())
                .authorProfileUrl(event.getAuthorProfileUrl())
                .restaurantId(event.getRestaurantId())
                .restaurantName(event.getRestaurantName())
                .reviewContent(event.getReviewContent())
                .rating(event.getRating())
                .imageUrls(event.getImageUrls())
                .groupId(event.getGroupId())
                .build();

        feedItemRepository.save(feedItem);
    }
}
