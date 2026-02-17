package com.example.map.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MarkerEventPublisherTest {

    @Mock
    private KafkaTemplate<String, MarkerCreatedEvent> kafkaTemplate;

    @InjectMocks
    private MarkerEventPublisher markerEventPublisher;

    @Test
    @DisplayName("marker-created 토픽으로 이벤트를 발행한다")
    void publishEvent() {
        MarkerCreatedEvent event = new MarkerCreatedEvent(1L, 1L, "맛집", "EXT_1", 10L);

        markerEventPublisher.publish(event);

        verify(kafkaTemplate).send("marker-created", event);
    }
}
