package com.example.map.service;

import com.example.map.dto.KakaoPlaceDto;

import java.util.List;

public interface KakaoLocalApiClient {

    List<KakaoPlaceDto> searchByKeyword(String keyword, Double x, Double y, Integer radius);
}
