package com.example.map.controller;

import com.example.map.dto.ReviewCreateRequest;
import com.example.map.dto.ReviewResponse;
import com.example.map.service.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(objectMapper);

        mockMvc = MockMvcBuilders.standaloneSetup(reviewController)
                .setMessageConverters(converter)
                .build();
    }

    @Test
    @DisplayName("POST /api/map/reviews 리뷰 등록 성공")
    void createReviewSuccess() throws Exception {
        ReviewCreateRequest request = new ReviewCreateRequest(
                "EXT_1", "맛집", "서울시", "한식", 127.0, 37.5,
                "맛있어요", 5, "PUBLIC", null);

        ReviewResponse response = new ReviewResponse(
                1L, "맛집", "EXT_1", 1L, "맛있어요", 5, "PUBLIC", List.of(), LocalDateTime.now());

        given(reviewService.createReview(any(ReviewCreateRequest.class), eq(1L), any()))
                .willReturn(response);

        MockMultipartFile reviewPart = new MockMultipartFile(
                "review", "", MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(request));

        mockMvc.perform(multipart("/api/map/reviews")
                        .file(reviewPart)
                        .header("X-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("리뷰 등록 성공"))
                .andExpect(jsonPath("$.data.restaurantName").value("맛집"));
    }

    @Test
    @DisplayName("GET /api/map/reviews/feed 피드 조회 성공")
    void getFeedSuccess() throws Exception {
        ReviewResponse response = new ReviewResponse(
                1L, "맛집", "EXT_1", 1L, "좋아요", 5, "GROUP", List.of(), LocalDateTime.now());

        given(reviewService.getFeed(eq(10L), any(Pageable.class)))
                .willReturn(new PageImpl<>(List.of(response)));

        mockMvc.perform(get("/api/map/reviews/feed")
                        .param("groupId", "10")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data[0].restaurantName").value("맛집"));
    }
}
