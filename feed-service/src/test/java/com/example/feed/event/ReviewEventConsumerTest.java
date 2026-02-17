package com.example.feed.event;

import com.example.common.dto.ReviewCreatedEvent;
import com.example.feed.service.FeedService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReviewEventConsumerTest {

    @Mock
    private FeedService feedService;

    @InjectMocks
    private ReviewEventConsumer reviewEventConsumer;

    @Test
    void consume_callsCreateFeedItem() {
        ReviewCreatedEvent event = ReviewCreatedEvent.builder()
                .authorId(100L)
                .authorNickname("홍길동")
                .restaurantId(200L)
                .restaurantName("맛있는 식당")
                .reviewContent("정말 맛있어요!")
                .rating(5)
                .groupId(10L)
                .build();

        reviewEventConsumer.consume(event);

        verify(feedService).createFeedItem(event);
    }
}
