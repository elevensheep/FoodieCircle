package com.example.map.repository;

import com.example.map.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    long countByRestaurantExternalId(String externalId);

    Page<Review> findByUserIdInAndGroupIdOrderByCreatedAtDesc(List<Long> userIds, Long groupId, Pageable pageable);
}
