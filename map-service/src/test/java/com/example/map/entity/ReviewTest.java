package com.example.map.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReviewTest {

    @Test
    @DisplayName("Review 엔티티를 생성할 수 있다")
    void createReview() {
        Restaurant restaurant = Restaurant.create("EXT_1", "맛집", "주소", "한식", 127.0, 37.5);
        Review review = Review.create(restaurant, 1L, "맛있어요", 5, Review.Visibility.PUBLIC, null);

        assertThat(review.getRestaurant()).isEqualTo(restaurant);
        assertThat(review.getUserId()).isEqualTo(1L);
        assertThat(review.getContent()).isEqualTo("맛있어요");
        assertThat(review.getRating()).isEqualTo(5);
        assertThat(review.getVisibility()).isEqualTo(Review.Visibility.PUBLIC);
        assertThat(review.getImages()).isEmpty();
        assertThat(review.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Review에 이미지를 추가할 수 있다")
    void addImage() {
        Restaurant restaurant = Restaurant.create("EXT_1", "맛집", "주소", "한식", 127.0, 37.5);
        Review review = Review.create(restaurant, 1L, "맛있어요", 5, Review.Visibility.GROUP, 10L);

        ReviewImage image = ReviewImage.create(review, "/uploads/img1.jpg");
        review.addImage(image);

        assertThat(review.getImages()).hasSize(1);
        assertThat(review.getImages().get(0).getImagePath()).isEqualTo("/uploads/img1.jpg");
        assertThat(review.getGroupId()).isEqualTo(10L);
    }
}
