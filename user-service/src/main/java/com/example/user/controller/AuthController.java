package com.example.user.controller;

import com.example.common.dto.ApiResponse;
import com.example.user.dto.KakaoLoginRequest;
import com.example.user.dto.KakaoLoginResponse;
import com.example.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login/kakao")
    public ApiResponse<KakaoLoginResponse> kakaoLogin(@Valid @RequestBody KakaoLoginRequest request) {
        KakaoLoginResponse response = authService.kakaoLogin(request.getAuthCode());
        return ApiResponse.success("로그인 성공", response);
    }
}
