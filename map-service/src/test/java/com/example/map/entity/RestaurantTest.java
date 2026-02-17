package com.example.map.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RestaurantTest {

    @Test
    @DisplayName("Restaurant 엔티티를 생성할 수 있다")
    void createRestaurant() {
        Restaurant restaurant = Restaurant.create("KAKAO_123", "맛집", "서울시 강남구", "한식", 127.0, 37.5);

        assertThat(restaurant.getExternalId()).isEqualTo("KAKAO_123");
        assertThat(restaurant.getName()).isEqualTo("맛집");
        assertThat(restaurant.getAddress()).isEqualTo("서울시 강남구");
        assertThat(restaurant.getCategory()).isEqualTo("한식");
        assertThat(restaurant.getX()).isEqualTo(127.0);
        assertThat(restaurant.getY()).isEqualTo(37.5);
        assertThat(restaurant.getCreatedAt()).isNotNull();
    }
}
