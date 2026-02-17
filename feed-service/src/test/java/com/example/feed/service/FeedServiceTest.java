package com.example.feed.service;

import com.example.common.dto.ReviewCreatedEvent;
import com.example.feed.dto.FeedItemResponse;
import com.example.feed.entity.FeedItem;
import com.example.feed.repository.FeedItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeedServiceTest {

    @Mock
    private FeedItemRepository feedItemRepository;

    @InjectMocks
    private FeedService feedService;

    private FeedItem sampleFeedItem;

    @BeforeEach
    void setUp() {
        sampleFeedItem = FeedItem.builder()
                .id(1L)
                .authorId(100L)
                .authorNickname("홍길동")
                .authorProfileUrl("https://example.com/profile.jpg")
                .restaurantId(200L)
                .restaurantName("맛있는 식당")
                .reviewContent("정말 맛있어요!")
                .rating(5)
                .imageUrls("img1.jpg,img2.jpg")
                .groupId(10L)
                .createdAt(LocalDateTime.of(2026, 2, 18, 12, 0))
                .build();
    }

    @Test
    void getFeed_withoutGroupId_returnsAllFeedItems() {
        Page<FeedItem> page = new PageImpl<>(List.of(sampleFeedItem));
        when(feedItemRepository.findAllByOrderByCreatedAtDesc(any(Pageable.class))).thenReturn(page);

        Page<FeedItemResponse> result = feedService.getFeed(null, 0, 10);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getAuthorNickname()).isEqualTo("홍길동");
        assertThat(result.getContent().get(0).getImageUrls()).containsExactly("img1.jpg", "img2.jpg");
        verify(feedItemRepository).findAllByOrderByCreatedAtDesc(PageRequest.of(0, 10));
    }

    @Test
    void getFeed_withGroupId_returnsFilteredFeedItems() {
        Page<FeedItem> page = new PageImpl<>(List.of(sampleFeedItem));
        when(feedItemRepository.findByGroupIdOrderByCreatedAtDesc(eq(10L), any(Pageable.class))).thenReturn(page);

        Page<FeedItemResponse> result = feedService.getFeed(10L, 0, 10);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getGroupId()).isEqualTo(10L);
        verify(feedItemRepository).findByGroupIdOrderByCreatedAtDesc(eq(10L), eq(PageRequest.of(0, 10)));
    }

    @Test
    void createFeedItem_savesEntityFromEvent() {
        ReviewCreatedEvent event = ReviewCreatedEvent.builder()
                .authorId(100L)
                .authorNickname("홍길동")
                .authorProfileUrl("https://example.com/profile.jpg")
                .restaurantId(200L)
                .restaurantName("맛있는 식당")
                .reviewContent("정말 맛있어요!")
                .rating(5)
                .imageUrls("img1.jpg,img2.jpg")
                .groupId(10L)
                .build();

        when(feedItemRepository.save(any(FeedItem.class))).thenReturn(sampleFeedItem);

        feedService.createFeedItem(event);

        ArgumentCaptor<FeedItem> captor = ArgumentCaptor.forClass(FeedItem.class);
        verify(feedItemRepository).save(captor.capture());

        FeedItem saved = captor.getValue();
        assertThat(saved.getAuthorId()).isEqualTo(100L);
        assertThat(saved.getRestaurantName()).isEqualTo("맛있는 식당");
        assertThat(saved.getGroupId()).isEqualTo(10L);
    }
}
