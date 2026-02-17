package com.example.map.controller;

import com.example.common.dto.ApiResponse;
import com.example.map.dto.RestaurantSearchResponse;
import com.example.map.service.RestaurantSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/map/restaurants")
@RequiredArgsConstructor
public class RestaurantSearchController {

    private final RestaurantSearchService restaurantSearchService;

    @GetMapping("/search")
    public ApiResponse<List<RestaurantSearchResponse>> search(
            @RequestParam String keyword,
            @RequestParam Double x,
            @RequestParam Double y,
            @RequestParam(defaultValue = "1000") Integer radius) {
        List<RestaurantSearchResponse> results = restaurantSearchService.search(keyword, x, y, radius);
        return ApiResponse.success("식당 검색 성공", results);
    }
}
