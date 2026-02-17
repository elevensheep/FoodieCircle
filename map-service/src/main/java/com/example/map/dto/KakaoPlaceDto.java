package com.example.map.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoPlaceDto {

    private String id;
    private String placeName;
    private String addressName;
    private String categoryName;
    private String x;
    private String y;
}
