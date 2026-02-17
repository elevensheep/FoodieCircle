package com.example.map.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CircleMemberTest {

    @Test
    @DisplayName("CircleMember 생성 시 circle과 userId가 설정된다")
    void createCircleMember() {
        Circle circle = Circle.create("모임", 1L);
        CircleMember member = CircleMember.create(circle, 2L);

        assertThat(member.getCircle()).isEqualTo(circle);
        assertThat(member.getUserId()).isEqualTo(2L);
        assertThat(member.getJoinedAt()).isNotNull();
    }
}
