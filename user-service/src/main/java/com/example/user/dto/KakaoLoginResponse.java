package com.example.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class KakaoLoginResponse {

    private String accessToken;
    private String refreshToken;
    private Long userId;
    private String nickname;
    private boolean isNewMember;
}
