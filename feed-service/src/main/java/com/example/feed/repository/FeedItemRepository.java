package com.example.feed.repository;

import com.example.feed.entity.FeedItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedItemRepository extends JpaRepository<FeedItem, Long> {

    Page<FeedItem> findByGroupIdOrderByCreatedAtDesc(Long groupId, Pageable pageable);

    Page<FeedItem> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
