package com.example.map.service;

import com.example.map.dto.ReviewCreateRequest;
import com.example.map.dto.ReviewResponse;
import com.example.map.entity.CircleMember;
import com.example.map.entity.Circle;
import com.example.map.entity.Restaurant;
import com.example.map.entity.Review;
import com.example.map.event.MarkerCreatedEvent;
import com.example.map.event.MarkerEventPublisher;
import com.example.map.repository.CircleMemberRepository;
import com.example.map.repository.RestaurantRepository;
import com.example.map.repository.ReviewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private CircleMemberRepository circleMemberRepository;
    @Mock
    private FileStorageService fileStorageService;
    @Mock
    private MarkerEventPublisher markerEventPublisher;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    @DisplayName("리뷰 생성 시 식당이 없으면 On-Demand로 생성한다")
    void createReviewCreatesRestaurantOnDemand() throws IOException {
        ReviewCreateRequest request = new ReviewCreateRequest(
                "EXT_NEW", "새맛집", "서울시", "한식", 127.0, 37.5,
                "맛있어요", 5, "PUBLIC", null);

        Restaurant restaurant = Restaurant.create("EXT_NEW", "새맛집", "서울시", "한식", 127.0, 37.5);
        Review review = Review.create(restaurant, 1L, "맛있어요", 5, Review.Visibility.PUBLIC, null);

        given(restaurantRepository.findByExternalId("EXT_NEW")).willReturn(Optional.empty());
        given(restaurantRepository.save(any(Restaurant.class))).willReturn(restaurant);
        given(reviewRepository.save(any(Review.class))).willReturn(review);

        ReviewResponse response = reviewService.createReview(request, 1L, null);

        assertThat(response.getRestaurantName()).isEqualTo("새맛집");
        verify(restaurantRepository).save(any(Restaurant.class));
        verify(markerEventPublisher).publish(any(MarkerCreatedEvent.class));
    }

    @Test
    @DisplayName("리뷰 생성 시 기존 식당이 있으면 재사용한다")
    void createReviewReusesExistingRestaurant() throws IOException {
        ReviewCreateRequest request = new ReviewCreateRequest(
                "EXT_1", "맛집", "서울시", "한식", 127.0, 37.5,
                "좋아요", 4, "GROUP", 10L);

        Restaurant restaurant = Restaurant.create("EXT_1", "맛집", "서울시", "한식", 127.0, 37.5);
        Review review = Review.create(restaurant, 1L, "좋아요", 4, Review.Visibility.GROUP, 10L);

        given(restaurantRepository.findByExternalId("EXT_1")).willReturn(Optional.of(restaurant));
        given(reviewRepository.save(any(Review.class))).willReturn(review);

        ReviewResponse response = reviewService.createReview(request, 1L, null);

        assertThat(response.getContent()).isEqualTo("좋아요");
    }

    @Test
    @DisplayName("이미지가 포함된 리뷰를 생성할 수 있다")
    void createReviewWithImages() throws IOException {
        ReviewCreateRequest request = new ReviewCreateRequest(
                "EXT_1", "맛집", "서울시", "한식", 127.0, 37.5,
                "사진후기", 5, "PUBLIC", null);

        Restaurant restaurant = Restaurant.create("EXT_1", "맛집", "서울시", "한식", 127.0, 37.5);
        Review review = Review.create(restaurant, 1L, "사진후기", 5, Review.Visibility.PUBLIC, null);

        given(restaurantRepository.findByExternalId("EXT_1")).willReturn(Optional.of(restaurant));
        given(fileStorageService.store(any())).willReturn("/uploads/uuid.jpg");
        given(reviewRepository.save(any(Review.class))).willReturn(review);

        MockMultipartFile image = new MockMultipartFile("images", "photo.jpg", "image/jpeg", "data".getBytes());
        reviewService.createReview(request, 1L, List.of(image));

        verify(fileStorageService).store(any());
    }

    @Test
    @DisplayName("피드 조회 시 그룹 멤버들의 리뷰를 페이징으로 반환한다")
    void getFeed() {
        Circle circle = Circle.create("모임", 1L);
        given(circleMemberRepository.findByCircleId(10L)).willReturn(List.of(
                CircleMember.create(circle, 1L),
                CircleMember.create(circle, 2L)
        ));

        Restaurant restaurant = Restaurant.create("EXT_1", "맛집", "서울시", "한식", 127.0, 37.5);
        Review review = Review.create(restaurant, 1L, "좋아요", 5, Review.Visibility.GROUP, 10L);

        given(reviewRepository.findByUserIdInAndGroupIdOrderByCreatedAtDesc(
                eq(List.of(1L, 2L)), eq(10L), any()))
                .willReturn(new PageImpl<>(List.of(review)));

        Page<ReviewResponse> feed = reviewService.getFeed(10L, PageRequest.of(0, 10));

        assertThat(feed.getContent()).hasSize(1);
    }
}
