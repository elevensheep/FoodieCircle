package com.example.map.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MarkerCreatedEvent {

    private Long reviewId;
    private Long userId;
    private String restaurantName;
    private String externalId;
    private Long groupId;
}
