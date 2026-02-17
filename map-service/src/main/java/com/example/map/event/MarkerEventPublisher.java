package com.example.map.event;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MarkerEventPublisher {

    private static final String TOPIC = "marker-created";

    private final KafkaTemplate<String, MarkerCreatedEvent> kafkaTemplate;

    public void publish(MarkerCreatedEvent event) {
        kafkaTemplate.send(TOPIC, event);
    }
}
