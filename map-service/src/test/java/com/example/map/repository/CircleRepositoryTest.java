package com.example.map.repository;

import com.example.map.entity.Circle;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class CircleRepositoryTest {

    @Autowired
    private CircleRepository circleRepository;

    @Test
    @DisplayName("Circle을 저장하고 ID로 조회할 수 있다")
    void saveAndFindById() {
        Circle circle = Circle.create("직장 점심", 1L);
        Circle saved = circleRepository.save(circle);

        assertThat(saved.getId()).isNotNull();
        assertThat(circleRepository.findById(saved.getId())).isPresent();
    }

    @Test
    @DisplayName("ownerId로 Circle 목록을 조회할 수 있다")
    void findByOwnerId() {
        circleRepository.save(Circle.create("모임1", 1L));
        circleRepository.save(Circle.create("모임2", 1L));
        circleRepository.save(Circle.create("다른사람", 2L));

        List<Circle> circles = circleRepository.findByOwnerId(1L);

        assertThat(circles).hasSize(2);
    }
}
