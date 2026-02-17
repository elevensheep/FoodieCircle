package com.example.feed.event;

import com.example.common.dto.ReviewCreatedEvent;
import com.example.feed.service.FeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReviewEventConsumer {

    private final FeedService feedService;

    @KafkaListener(topics = "review-created", containerFactory = "kafkaListenerContainerFactory")
    public void consume(ReviewCreatedEvent event) {
        log.info("Received review-created event: authorId={}, restaurantId={}",
                event.getAuthorId(), event.getRestaurantId());
        feedService.createFeedItem(event);
    }
}
