package com.example.user.service;

import com.example.user.client.KakaoApiClient;
import com.example.user.dto.KakaoLoginResponse;
import com.example.user.dto.KakaoTokenResponse;
import com.example.user.dto.KakaoUserInfoResponse;
import com.example.user.entity.User;
import com.example.user.repository.UserRepository;
import com.example.user.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final KakaoApiClient kakaoApiClient;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public KakaoLoginResponse kakaoLogin(String authCode) {
        KakaoTokenResponse kakaoToken = kakaoApiClient.getAccessToken(authCode);
        KakaoUserInfoResponse userInfo = kakaoApiClient.getUserInfo(kakaoToken.getAccessToken());

        boolean isNewMember = false;
        User user = userRepository.findByKakaoId(userInfo.getId()).orElse(null);

        if (user == null) {
            isNewMember = true;
            user = User.builder()
                    .kakaoId(userInfo.getId())
                    .nickname(userInfo.getNickname())
                    .email(userInfo.getEmail())
                    .profileImageUrl(userInfo.getProfileImageUrl())
                    .build();
            user = userRepository.save(user);
        } else {
            user.updateProfile(
                    userInfo.getNickname(),
                    userInfo.getEmail(),
                    userInfo.getProfileImageUrl()
            );
        }

        String accessToken = jwtTokenProvider.generateAccessToken(user.getUuid().toString());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUuid().toString());

        return KakaoLoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .nickname(user.getNickname())
                .isNewMember(isNewMember)
                .build();
    }
}
