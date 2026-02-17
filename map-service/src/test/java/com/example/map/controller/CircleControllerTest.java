package com.example.map.controller;

import com.example.map.dto.CircleCreateRequest;
import com.example.map.dto.CircleResponse;
import com.example.map.service.CircleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CircleControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private CircleService circleService;

    @InjectMocks
    private CircleController circleController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(circleController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("POST /api/map/groups 그룹 생성 성공")
    void createCircleSuccess() throws Exception {
        CircleResponse response = new CircleResponse(1L, "직장 점심", 1L, 1, LocalDateTime.now());

        given(circleService.createCircle(any(CircleCreateRequest.class), eq(1L)))
                .willReturn(response);

        mockMvc.perform(post("/api/map/groups")
                        .header("X-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CircleCreateRequest("직장 점심"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("그룹 생성 성공"))
                .andExpect(jsonPath("$.data.name").value("직장 점심"));
    }

    @Test
    @DisplayName("GET /api/map/groups 내 그룹 목록 조회 성공")
    void getMyCirclesSuccess() throws Exception {
        CircleResponse response = new CircleResponse(1L, "모임", 1L, 3, LocalDateTime.now());

        given(circleService.getMyCircles(1L)).willReturn(List.of(response));

        mockMvc.perform(get("/api/map/groups")
                        .header("X-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].name").value("모임"));
    }
}
