package com.example.schedule.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class HealthControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new HealthController()).build();
    }

    @Test
    @DisplayName("GET /api/schedule/health - 정상 응답 확인")
    void health_returnsOkStatus() throws Exception {
        mockMvc.perform(get("/api/schedule/health"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("schedule-service is running"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    @DisplayName("POST /api/schedule/health - 허용되지 않은 메서드 405 반환")
    void health_postMethodNotAllowed() throws Exception {
        mockMvc.perform(post("/api/schedule/health"))
                .andExpect(status().isMethodNotAllowed());
    }
}
