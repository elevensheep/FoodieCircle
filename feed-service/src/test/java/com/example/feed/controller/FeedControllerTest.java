package com.example.feed.controller;

import com.example.feed.dto.FeedItemResponse;
import com.example.feed.service.FeedService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class FeedControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FeedService feedService;

    @InjectMocks
    private FeedController feedController;

    private FeedItemResponse sampleResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(feedController).build();

        sampleResponse = FeedItemResponse.builder()
                .id(1L)
                .authorId(100L)
                .authorNickname("홍길동")
                .authorProfileUrl("https://example.com/profile.jpg")
                .restaurantId(200L)
                .restaurantName("맛있는 식당")
                .reviewContent("정말 맛있어요!")
                .rating(5)
                .imageUrls(List.of("img1.jpg", "img2.jpg"))
                .groupId(10L)
                .createdAt(LocalDateTime.of(2026, 2, 18, 12, 0))
                .build();
    }

    @Test
    void getFeed_withoutGroupId_returnsAllFeed() throws Exception {
        Page<FeedItemResponse> page = new PageImpl<>(List.of(sampleResponse));
        when(feedService.getFeed(null, 0, 10)).thenReturn(page);

        mockMvc.perform(get("/api/feed/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("피드 조회 성공"))
                .andExpect(jsonPath("$.data.content[0].authorNickname").value("홍길동"))
                .andExpect(jsonPath("$.data.content[0].restaurantName").value("맛있는 식당"))
                .andExpect(jsonPath("$.data.content[0].imageUrls[0]").value("img1.jpg"));

        verify(feedService).getFeed(null, 0, 10);
    }

    @Test
    void getFeed_withGroupId_returnsFilteredFeed() throws Exception {
        Page<FeedItemResponse> page = new PageImpl<>(List.of(sampleResponse));
        when(feedService.getFeed(10L, 0, 10)).thenReturn(page);

        mockMvc.perform(get("/api/feed/reviews").param("groupId", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.content[0].groupId").value(10));

        verify(feedService).getFeed(10L, 0, 10);
    }

    @Test
    void getFeed_withCustomPageAndSize_usesProvidedValues() throws Exception {
        Page<FeedItemResponse> page = new PageImpl<>(List.of(sampleResponse));
        when(feedService.getFeed(null, 2, 5)).thenReturn(page);

        mockMvc.perform(get("/api/feed/reviews")
                        .param("page", "2")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));

        verify(feedService).getFeed(null, 2, 5);
    }
}
