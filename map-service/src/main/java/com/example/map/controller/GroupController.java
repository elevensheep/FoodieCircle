package com.example.map.controller;

import com.example.common.dto.ApiResponse;
import com.example.map.dto.GroupCreateRequest;
import com.example.map.dto.GroupResponse;
import com.example.map.service.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/map")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping("/groups")
    public ApiResponse<GroupResponse> createGroup(
            @RequestHeader("X-USER-ID") Long userId,
            @Valid @RequestBody GroupCreateRequest request) {
        GroupResponse response = groupService.createGroup(userId, request);
        return ApiResponse.success("그룹이 생성되었습니다", response);
    }

    @GetMapping("/groups")
    public ApiResponse<List<GroupResponse>> getMyGroups(
            @RequestHeader("X-USER-ID") Long userId) {
        List<GroupResponse> responses = groupService.getMyGroups(userId);
        return ApiResponse.success("내 그룹 목록 조회 성공", responses);
    }
}
