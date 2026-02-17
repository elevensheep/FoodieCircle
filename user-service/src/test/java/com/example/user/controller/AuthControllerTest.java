package com.example.user.controller;

import com.example.user.dto.KakaoLoginResponse;
import com.example.user.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void kakaoLogin_성공() throws Exception {
        KakaoLoginResponse loginResponse = KakaoLoginResponse.builder()
                .accessToken("jwt-access-token")
                .refreshToken("jwt-refresh-token")
                .userId(1L)
                .nickname("테스터")
                .isNewMember(true)
                .build();

        given(authService.kakaoLogin("test-auth-code")).willReturn(loginResponse);

        mockMvc.perform(post("/api/user/auth/login/kakao")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"authCode\":\"test-auth-code\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.accessToken").value("jwt-access-token"))
                .andExpect(jsonPath("$.data.refreshToken").value("jwt-refresh-token"))
                .andExpect(jsonPath("$.data.userId").value(1))
                .andExpect(jsonPath("$.data.nickname").value("테스터"))
                .andExpect(jsonPath("$.data.newMember").value(true));
    }

    @Test
    @WithMockUser
    void kakaoLogin_인가코드_누락시_400에러() throws Exception {
        mockMvc.perform(post("/api/user/auth/login/kakao")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"authCode\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void kakaoLogin_요청본문_없을시_400에러() throws Exception {
        mockMvc.perform(post("/api/user/auth/login/kakao")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}
