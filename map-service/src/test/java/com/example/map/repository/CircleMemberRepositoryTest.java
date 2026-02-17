package com.example.map.repository;

import com.example.map.entity.Circle;
import com.example.map.entity.CircleMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class CircleMemberRepositoryTest {

    @Autowired
    private CircleRepository circleRepository;

    @Autowired
    private CircleMemberRepository circleMemberRepository;

    private Circle circle;

    @BeforeEach
    void setUp() {
        circle = circleRepository.save(Circle.create("테스트 그룹", 1L));
    }

    @Test
    @DisplayName("CircleMember를 저장하고 userId로 조회할 수 있다")
    void saveAndFindByUserId() {
        circleMemberRepository.save(CircleMember.create(circle, 1L));
        circleMemberRepository.save(CircleMember.create(circle, 2L));

        List<CircleMember> members = circleMemberRepository.findByUserId(1L);

        assertThat(members).hasSize(1);
        assertThat(members.get(0).getCircle().getId()).isEqualTo(circle.getId());
    }

    @Test
    @DisplayName("circleId로 멤버 목록을 조회할 수 있다")
    void findByCircleId() {
        circleMemberRepository.save(CircleMember.create(circle, 1L));
        circleMemberRepository.save(CircleMember.create(circle, 2L));

        List<CircleMember> members = circleMemberRepository.findByCircleId(circle.getId());

        assertThat(members).hasSize(2);
    }

    @Test
    @DisplayName("circleId와 userId로 멤버 존재 여부를 확인할 수 있다")
    void existsByCircleIdAndUserId() {
        circleMemberRepository.save(CircleMember.create(circle, 5L));

        assertThat(circleMemberRepository.existsByCircleIdAndUserId(circle.getId(), 5L)).isTrue();
        assertThat(circleMemberRepository.existsByCircleIdAndUserId(circle.getId(), 999L)).isFalse();
    }
}
