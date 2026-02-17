package com.example.map.controller;

import com.example.common.dto.ApiResponse;
import com.example.map.dto.CircleCreateRequest;
import com.example.map.dto.CircleResponse;
import com.example.map.service.CircleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/map/groups")
@RequiredArgsConstructor
public class CircleController {

    private final CircleService circleService;

    @PostMapping
    public ApiResponse<CircleResponse> createCircle(
            @Valid @RequestBody CircleCreateRequest request,
            @RequestHeader("X-User-Id") Long userId) {
        CircleResponse response = circleService.createCircle(request, userId);
        return ApiResponse.success("그룹 생성 성공", response);
    }

    @GetMapping
    public ApiResponse<List<CircleResponse>> getMyCircles(
            @RequestHeader("X-User-Id") Long userId) {
        List<CircleResponse> circles = circleService.getMyCircles(userId);
        return ApiResponse.success("내 그룹 목록 조회 성공", circles);
    }
}
