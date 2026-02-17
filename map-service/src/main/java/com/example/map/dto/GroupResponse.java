package com.example.map.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GroupResponse {

    private Long groupId;
    private String name;
    private int memberCount;
    private Long ownerId;
}
