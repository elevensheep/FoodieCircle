package com.example.map.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RestaurantSearchResponse {

    private String externalId;
    private String name;
    private String address;
    private String category;
    private Double x;
    private Double y;
    private long reviewCount;
}
