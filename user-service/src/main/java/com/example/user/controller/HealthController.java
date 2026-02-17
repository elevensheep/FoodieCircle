package com.example.user.controller;

import com.example.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Health", description = "서비스 상태 확인 API")
@RestController
@RequestMapping("/api/user")
public class HealthController {

    @Operation(summary = "헬스 체크", description = "User Service의 동작 상태를 확인합니다")
    @GetMapping("/health")
    public ApiResponse<Void> health() {
        return ApiResponse.success("user-service is running");
    }
}
