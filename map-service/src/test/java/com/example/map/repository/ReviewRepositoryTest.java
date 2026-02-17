package com.example.map.repository;

import com.example.map.entity.Restaurant;
import com.example.map.entity.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        restaurant = restaurantRepository.save(
                Restaurant.create("EXT_1", "맛집", "서울시", "한식", 127.0, 37.5)
        );
    }

    @Test
    @DisplayName("externalId로 리뷰 수를 집계할 수 있다")
    void countByRestaurantExternalId() {
        reviewRepository.save(Review.create(restaurant, 1L, "좋아요", 5, Review.Visibility.PUBLIC, null));
        reviewRepository.save(Review.create(restaurant, 2L, "맛있어요", 4, Review.Visibility.PUBLIC, null));

        long count = reviewRepository.countByRestaurantExternalId("EXT_1");

        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("userId 목록과 groupId로 리뷰를 페이징 조회할 수 있다")
    void findByUserIdInAndGroupId() {
        reviewRepository.save(Review.create(restaurant, 1L, "리뷰1", 5, Review.Visibility.GROUP, 10L));
        reviewRepository.save(Review.create(restaurant, 2L, "리뷰2", 4, Review.Visibility.GROUP, 10L));
        reviewRepository.save(Review.create(restaurant, 3L, "리뷰3", 3, Review.Visibility.GROUP, 10L));
        reviewRepository.save(Review.create(restaurant, 99L, "다른그룹", 3, Review.Visibility.GROUP, 20L));

        Page<Review> page = reviewRepository.findByUserIdInAndGroupIdOrderByCreatedAtDesc(
                List.of(1L, 2L), 10L, PageRequest.of(0, 10));

        assertThat(page.getContent()).hasSize(2);
    }
}
