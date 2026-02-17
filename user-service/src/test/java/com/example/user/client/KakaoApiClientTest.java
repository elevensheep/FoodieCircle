package com.example.user.client;

import com.example.user.dto.KakaoTokenResponse;
import com.example.user.dto.KakaoUserInfoResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;

class KakaoApiClientTest {

    private KakaoApiClient kakaoApiClient;
    private MockRestServiceServer mockServer;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        RestTemplate restTemplate = new RestTemplate();
        mockServer = MockRestServiceServer.createServer(restTemplate);
        kakaoApiClient = new KakaoApiClient(
                restTemplate,
                "test-client-id",
                "http://localhost/callback",
                "https://kauth.kakao.com/oauth/token",
                "https://kapi.kakao.com/v2/user/me"
        );
    }

    @Test
    void getAccessToken_인가코드로_토큰_교환_성공() throws JsonProcessingException {
        String tokenJson = """
                {
                    "access_token": "mock-access-token",
                    "token_type": "bearer",
                    "refresh_token": "mock-refresh-token",
                    "expires_in": 7200,
                    "refresh_token_expires_in": 5184000
                }
                """;

        mockServer.expect(requestTo("https://kauth.kakao.com/oauth/token"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(tokenJson, MediaType.APPLICATION_JSON));

        KakaoTokenResponse response = kakaoApiClient.getAccessToken("test-auth-code");

        assertThat(response.getAccessToken()).isEqualTo("mock-access-token");
        assertThat(response.getRefreshToken()).isEqualTo("mock-refresh-token");
        mockServer.verify();
    }

    @Test
    void getUserInfo_액세스토큰으로_사용자정보_조회_성공() {
        String userInfoJson = """
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
                """;

        mockServer.expect(requestTo("https://kapi.kakao.com/v2/user/me"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("Authorization", "Bearer mock-access-token"))
                .andRespond(withSuccess(userInfoJson, MediaType.APPLICATION_JSON));

        KakaoUserInfoResponse response = kakaoApiClient.getUserInfo("mock-access-token");

        assertThat(response.getId()).isEqualTo(12345L);
        assertThat(response.getNickname()).isEqualTo("테스터");
        assertThat(response.getEmail()).isEqualTo("test@kakao.com");
        assertThat(response.getProfileImageUrl()).isEqualTo("http://img.kakao.com/profile.jpg");
        mockServer.verify();
    }

    @Test
    void getAccessToken_카카오API_실패시_예외_발생() {
        mockServer.expect(requestTo("https://kauth.kakao.com/oauth/token"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withServerError());

        assertThatThrownBy(() -> kakaoApiClient.getAccessToken("invalid-code"))
                .isInstanceOf(RuntimeException.class);
        mockServer.verify();
    }
}
