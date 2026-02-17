package com.example.map.repository;

import com.example.map.entity.CircleMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CircleMemberRepository extends JpaRepository<CircleMember, Long> {

    List<CircleMember> findByUserId(Long userId);

    List<CircleMember> findByCircleId(Long circleId);

    boolean existsByCircleIdAndUserId(Long circleId, Long userId);
}
