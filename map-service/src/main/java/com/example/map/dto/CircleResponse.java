package com.example.map.dto;

import com.example.map.entity.Circle;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CircleResponse {

    private Long id;
    private String name;
    private Long ownerId;
    private int memberCount;
    private LocalDateTime createdAt;

    public static CircleResponse from(Circle circle) {
        return new CircleResponse(
                circle.getId(),
                circle.getName(),
                circle.getOwnerId(),
                circle.getMembers().size(),
                circle.getCreatedAt()
        );
    }
}
