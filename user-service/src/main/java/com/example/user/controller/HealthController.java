package com.example.user.controller;

import com.example.common.dto.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class HealthController {

    @GetMapping("/health")
    public ApiResponse<Void> health() {
        return ApiResponse.success("user-service is running");
    }
}
