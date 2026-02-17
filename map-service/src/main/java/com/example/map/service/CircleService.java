package com.example.map.service;

import com.example.map.dto.CircleCreateRequest;
import com.example.map.dto.CircleResponse;
import com.example.map.entity.Circle;
import com.example.map.entity.CircleMember;
import com.example.map.repository.CircleMemberRepository;
import com.example.map.repository.CircleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CircleService {

    private final CircleRepository circleRepository;
    private final CircleMemberRepository circleMemberRepository;

    @Transactional
    public CircleResponse createCircle(CircleCreateRequest request, Long userId) {
        Circle circle = Circle.create(request.getName(), userId);
        Circle saved = circleRepository.save(circle);

        CircleMember ownerMember = CircleMember.create(saved, userId);
        circleMemberRepository.save(ownerMember);

        return CircleResponse.from(saved);
    }

    public List<CircleResponse> getMyCircles(Long userId) {
        List<Circle> ownedCircles = circleRepository.findByOwnerId(userId);

        List<Circle> memberCircles = circleMemberRepository.findByUserId(userId).stream()
                .map(CircleMember::getCircle)
                .toList();

        return Stream.concat(ownedCircles.stream(), memberCircles.stream())
                .distinct()
                .map(CircleResponse::from)
                .toList();
    }
}
