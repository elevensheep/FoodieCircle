package com.example.user.service;

import com.example.user.client.KakaoApiClient;
import com.example.user.dto.KakaoLoginResponse;
import com.example.user.dto.KakaoTokenResponse;
import com.example.user.dto.KakaoUserInfoResponse;
import com.example.user.entity.User;
import com.example.user.repository.UserRepository;
import com.example.user.security.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private KakaoApiClient kakaoApiClient;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthService authService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void kakaoLogin_신규회원_회원가입_및_JWT_발급() throws Exception {
        // given
        String authCode = "test-auth-code";

        KakaoTokenResponse tokenResponse = objectMapper.readValue("""
                {
                    "access_token": "kakao-access-token",
                    "token_type": "bearer",
                    "refresh_token": "kakao-refresh-token",
                    "expires_in": 7200,
                    "refresh_token_expires_in": 5184000
                }
                """, KakaoTokenResponse.class);

        KakaoUserInfoResponse userInfoResponse = objectMapper.readValue("""
                {
                    "id": 12345,
                    "kakao_account": {
                        "email": "test@kakao.com",
                        "profile": {
                            "nickname": "테스터",
                            "profile_image_url": "http://img.kakao.com/profile.jpg"
                        }
                    }
                }
                """, KakaoUserInfoResponse.class);

        User newUser = User.builder()
                .kakaoId(12345L)
                .nickname("테스터")
                .email("test@kakao.com")
                .profileImageUrl("http://img.kakao.com/profile.jpg")
                .build();

        given(kakaoApiClient.getAccessToken(authCode)).willReturn(tokenResponse);
        given(kakaoApiClient.getUserInfo("kakao-access-token")).willReturn(userInfoResponse);
        given(userRepository.findByKakaoId(12345L)).willReturn(Optional.empty());
        given(userRepository.save(any(User.class))).willReturn(newUser);
        given(jwtTokenProvider.generateAccessToken(anyString())).willReturn("jwt-access-token");
        given(jwtTokenProvider.generateRefreshToken(anyString())).willReturn("jwt-refresh-token");

        // when
        KakaoLoginResponse response = authService.kakaoLogin(authCode);

        // then
        assertThat(response.getAccessToken()).isEqualTo("jwt-access-token");
        assertThat(response.getRefreshToken()).isEqualTo("jwt-refresh-token");
        assertThat(response.getNickname()).isEqualTo("테스터");
        assertThat(response.isNewMember()).isTrue();
        verify(userRepository).save(any(User.class));
    }

    @Test
    void kakaoLogin_기존회원_정보업데이트_및_JWT_발급() throws Exception {
        // given
        String authCode = "test-auth-code";

        KakaoTokenResponse tokenResponse = objectMapper.readValue("""
                {
                    "access_token": "kakao-access-token",
                    "token_type": "bearer",
                    "refresh_token": "kakao-refresh-token",
                    "expires_in": 7200,
                    "refresh_token_expires_in": 5184000
                }
                """, KakaoTokenResponse.class);

        KakaoUserInfoResponse userInfoResponse = objectMapper.readValue("""
                {
                    "id": 12345,
                    "kakao_account": {
                        "email": "updated@kakao.com",
                        "profile": {
                            "nickname": "새닉네임",
                            "profile_image_url": "http://img.kakao.com/new.jpg"
                        }
                    }
                }
                """, KakaoUserInfoResponse.class);

        User existingUser = User.builder()
                .kakaoId(12345L)
                .nickname("원래닉네임")
                .email("old@kakao.com")
                .build();

        given(kakaoApiClient.getAccessToken(authCode)).willReturn(tokenResponse);
        given(kakaoApiClient.getUserInfo("kakao-access-token")).willReturn(userInfoResponse);
        given(userRepository.findByKakaoId(12345L)).willReturn(Optional.of(existingUser));
        given(jwtTokenProvider.generateAccessToken(anyString())).willReturn("jwt-access-token");
        given(jwtTokenProvider.generateRefreshToken(anyString())).willReturn("jwt-refresh-token");

        // when
        KakaoLoginResponse response = authService.kakaoLogin(authCode);

        // then
        assertThat(response.getAccessToken()).isEqualTo("jwt-access-token");
        assertThat(response.isNewMember()).isFalse();
        assertThat(existingUser.getNickname()).isEqualTo("새닉네임");
        assertThat(existingUser.getEmail()).isEqualTo("updated@kakao.com");
    }

    @Test
    void kakaoLogin_카카오API_실패시_예외_전파() {
        // given
        given(kakaoApiClient.getAccessToken("bad-code"))
                .willThrow(new RuntimeException("카카오 API 호출 실패"));

        // when & then
        assertThatThrownBy(() -> authService.kakaoLogin("bad-code"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("카카오 API 호출 실패");
    }
}
