package com.example.map.controller;

import com.example.common.dto.ApiResponse;
import com.example.map.dto.ReviewCreateRequest;
import com.example.map.dto.ReviewResponse;
import com.example.map.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/map/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ApiResponse<ReviewResponse> createReview(
            @Valid @RequestPart("review") ReviewCreateRequest request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestHeader("X-User-Id") Long userId) throws IOException {
        ReviewResponse response = reviewService.createReview(request, userId, images);
        return ApiResponse.success("리뷰 등록 성공", response);
    }

    @GetMapping("/feed")
    public ApiResponse<List<ReviewResponse>> getFeed(
            @RequestParam Long groupId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ReviewResponse> feed = reviewService.getFeed(groupId, PageRequest.of(page, size));
        return ApiResponse.success("피드 조회 성공", feed.getContent());
    }
}
