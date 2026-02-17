package com.example.map.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CircleTest {

    @Test
    @DisplayName("Circle 엔티티 생성 시 name과 ownerId가 설정된다")
    void createCircle() {
        Circle circle = Circle.create("직장 점심", 1L);

        assertThat(circle.getName()).isEqualTo("직장 점심");
        assertThat(circle.getOwnerId()).isEqualTo(1L);
        assertThat(circle.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Circle 엔티티의 members 리스트는 빈 리스트로 초기화된다")
    void circleHasEmptyMembers() {
        Circle circle = Circle.create("모임", 2L);

        assertThat(circle.getMembers()).isNotNull().isEmpty();
    }
}
