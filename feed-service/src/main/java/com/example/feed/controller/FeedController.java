package com.example.feed.controller;

import com.example.common.dto.ApiResponse;
import com.example.feed.dto.FeedItemResponse;
import com.example.feed.dto.FeedPageResponse;
import com.example.feed.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/feed")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    @GetMapping("/reviews")
    public ApiResponse<FeedPageResponse> getFeed(
            @RequestParam(required = false) Long groupId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<FeedItemResponse> feed = feedService.getFeed(groupId, page, size);
        return ApiResponse.success("피드 조회 성공", FeedPageResponse.from(feed));
    }
}
