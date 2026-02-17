package com.example.map.service;

import com.example.map.dto.CircleCreateRequest;
import com.example.map.dto.CircleResponse;
import com.example.map.entity.Circle;
import com.example.map.entity.CircleMember;
import com.example.map.repository.CircleMemberRepository;
import com.example.map.repository.CircleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CircleServiceTest {

    @Mock
    private CircleRepository circleRepository;

    @Mock
    private CircleMemberRepository circleMemberRepository;

    @InjectMocks
    private CircleService circleService;

    @Test
    @DisplayName("그룹 생성 시 owner도 멤버로 자동 추가된다")
    void createCircleAddsOwnerAsMember() {
        CircleCreateRequest request = new CircleCreateRequest("직장 점심");
        Circle circle = Circle.create("직장 점심", 1L);

        given(circleRepository.save(any(Circle.class))).willReturn(circle);
        given(circleMemberRepository.save(any(CircleMember.class)))
                .willReturn(CircleMember.create(circle, 1L));

        CircleResponse response = circleService.createCircle(request, 1L);

        assertThat(response.getName()).isEqualTo("직장 점심");
        assertThat(response.getOwnerId()).isEqualTo(1L);
        verify(circleMemberRepository).save(any(CircleMember.class));
    }

    @Test
    @DisplayName("내 그룹 목록은 owner + member인 그룹을 중복 제거하여 반환한다")
    void getMyCirclesDeduplicates() {
        Circle circle1 = Circle.create("모임1", 1L);
        Circle circle2 = Circle.create("모임2", 2L);

        given(circleRepository.findByOwnerId(1L)).willReturn(List.of(circle1));

        CircleMember member = CircleMember.create(circle2, 1L);
        given(circleMemberRepository.findByUserId(1L)).willReturn(List.of(
                CircleMember.create(circle1, 1L),
                member
        ));

        List<CircleResponse> results = circleService.getMyCircles(1L);

        assertThat(results).hasSize(2);
    }
}
