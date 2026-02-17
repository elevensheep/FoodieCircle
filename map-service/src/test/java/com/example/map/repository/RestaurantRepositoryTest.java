package com.example.map.repository;

import com.example.map.entity.Restaurant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class RestaurantRepositoryTest {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    @DisplayName("Restaurant을 저장하고 externalId로 조회할 수 있다")
    void findByExternalId() {
        restaurantRepository.save(Restaurant.create("KAKAO_123", "맛집", "서울시", "한식", 127.0, 37.5));

        Optional<Restaurant> found = restaurantRepository.findByExternalId("KAKAO_123");

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("맛집");
    }

    @Test
    @DisplayName("존재하지 않는 externalId는 빈 Optional을 반환한다")
    void findByExternalIdNotFound() {
        Optional<Restaurant> found = restaurantRepository.findByExternalId("NONEXIST");

        assertThat(found).isEmpty();
    }
}
