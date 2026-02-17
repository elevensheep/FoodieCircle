package com.example.map.service;

import com.example.map.dto.KakaoPlaceDto;
import com.example.map.dto.RestaurantSearchResponse;
import com.example.map.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantSearchService {

    private final KakaoLocalApiClient kakaoLocalApiClient;
    private final ReviewRepository reviewRepository;

    public List<RestaurantSearchResponse> search(String keyword, Double x, Double y, Integer radius) {
        List<KakaoPlaceDto> places = kakaoLocalApiClient.searchByKeyword(keyword, x, y, radius);

        return places.stream()
                .map(place -> new RestaurantSearchResponse(
                        place.getId(),
                        place.getPlaceName(),
                        place.getAddressName(),
                        place.getCategoryName(),
                        Double.parseDouble(place.getX()),
                        Double.parseDouble(place.getY()),
                        reviewRepository.countByRestaurantExternalId(place.getId())
                ))
                .toList();
    }
}
