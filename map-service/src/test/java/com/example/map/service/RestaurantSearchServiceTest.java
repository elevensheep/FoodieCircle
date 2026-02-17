package com.example.map.service;

import com.example.map.dto.KakaoPlaceDto;
import com.example.map.dto.RestaurantSearchResponse;
import com.example.map.repository.ReviewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class RestaurantSearchServiceTest {

    @Mock
    private KakaoLocalApiClient kakaoLocalApiClient;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private RestaurantSearchService restaurantSearchService;

    @Test
    @DisplayName("키워드 검색 시 카카오 API 결과에 내부 리뷰 수를 병합하여 반환한다")
    void searchMergesReviewCount() {
        KakaoPlaceDto place1 = new KakaoPlaceDto("K1", "맛집1", "서울시 강남구", "한식", "127.0", "37.5");
        KakaoPlaceDto place2 = new KakaoPlaceDto("K2", "맛집2", "서울시 서초구", "일식", "127.1", "37.6");

        given(kakaoLocalApiClient.searchByKeyword("맛집", 127.0, 37.5, 1000))
                .willReturn(List.of(place1, place2));
        given(reviewRepository.countByRestaurantExternalId("K1")).willReturn(3L);
        given(reviewRepository.countByRestaurantExternalId("K2")).willReturn(0L);

        List<RestaurantSearchResponse> results = restaurantSearchService.search("맛집", 127.0, 37.5, 1000);

        assertThat(results).hasSize(2);
        assertThat(results.get(0).getExternalId()).isEqualTo("K1");
        assertThat(results.get(0).getReviewCount()).isEqualTo(3);
        assertThat(results.get(1).getReviewCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("검색 결과가 없으면 빈 리스트를 반환한다")
    void searchReturnsEmptyList() {
        given(kakaoLocalApiClient.searchByKeyword("없는곳", 127.0, 37.5, 1000))
                .willReturn(List.of());

        List<RestaurantSearchResponse> results = restaurantSearchService.search("없는곳", 127.0, 37.5, 1000);

        assertThat(results).isEmpty();
    }
}
